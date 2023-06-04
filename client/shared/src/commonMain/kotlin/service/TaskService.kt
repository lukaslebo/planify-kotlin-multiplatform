package service

import androidx.compose.runtime.mutableStateListOf
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.websocket.*
import io.ktor.serialization.kotlinx.KotlinxWebsocketSerializationConverter
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.*
import kotlinx.serialization.json.Json
import task.Task
import task.TaskRequestMessage
import task.TaskResponseMessage

private const val protocol = "ws://"
private const val host = "localhost"
private const val port = 8443

fun url(path: String) = "$protocol$host:$port$path"

private val websocketScope = CoroutineScope(Dispatchers.Default)

private val client = HttpClient {
    expectSuccess = false
    install(WebSockets) {
        contentConverter = KotlinxWebsocketSerializationConverter(Json)
    }
    install(ContentNegotiation) {
        json()
    }
}

private var websocketJob: Job? = null
private var websocketSession: DefaultClientWebSocketSession? = null

object TaskService {
    val tasks = mutableStateListOf<Task>()

    fun createTask(task: Task) {
        TaskRequestMessage.CreateNewTask(task).execute()
    }

    fun updateTask(task: Task) {
        TaskRequestMessage.UpdateTask(task).execute()
    }

    fun deleteTask(task: Task) {
        TaskRequestMessage.DeleteTask(task.id).execute()
    }

    private fun TaskRequestMessage.execute() {
        websocketScope.launch { websocketSession?.sendSerialized(this@execute) }
    }

    private fun TaskResponseMessage.update(state: MutableList<Task>) {
        when (this) {
            is TaskResponseMessage.AllTasks -> {
                state.clear()
                state += tasks
            }

            is TaskResponseMessage.CreatedTask -> {
                state += task
            }

            is TaskResponseMessage.UpdatedTask -> {
                val updateIndex = state.indexOfFirst { it.id == task.id }
                if (updateIndex > -1) {
                    state[updateIndex] = task
                }
            }

            is TaskResponseMessage.DeletedTask -> {
                val removeIndex = state.indexOfFirst { it.id == taskId }
                if (removeIndex > -1) {
                    state.removeAt(removeIndex)
                }
            }
        }
    }

    fun startAutoReconnect() {
        suspend fun connect() = client.webSocket(url("/tasks")) {
            println("opened websocket session")
            closeReason.invokeOnCompletion { websocketSession = null }
            websocketSession = this

            while (isActive) {
                val response = receiveDeserialized<TaskResponseMessage>()
                response.update(tasks)
            }
        }

        println("Starting Auto-Reconnect")
        closeConnection()
        websocketJob = websocketScope.launch {
            while (isActive) {
                try {
                    connect()
                } catch (e: Exception) {
                    println("Connection failed with:")
                    e.printStackTrace()
                }
                delay(1000)
            }
        }
    }

    fun closeConnection() {
//        runBlocking { websocketJob?.cancelAndJoin() } // not yet implemented on multiplatform
        websocketJob?.cancel()
    }
}

