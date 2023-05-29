package server

import io.ktor.client.plugins.websocket.receiveDeserialized
import io.ktor.client.plugins.websocket.sendSerialized
import io.ktor.client.plugins.websocket.webSocket
import io.ktor.serialization.kotlinx.KotlinxWebsocketSerializationConverter
import io.ktor.server.testing.testApplication
import io.ktor.websocket.close
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.Test
import task.Task
import task.TaskRequestMessage
import task.TaskResponseMessage
import task.TaskStatus
import kotlin.test.assertIs
import kotlin.test.assertTrue
import io.ktor.client.plugins.websocket.WebSockets as WebSocketsClient

@OptIn(ExperimentalCoroutinesApi::class)
class PlanifyServerTest {
    @Test
    fun `tasks websocket should emit AllTasks on connection and then propagate all events on all connections`() =
        testApplication {
            application { planifyServerModule() }

            val client = createClient {
                this@createClient.install(WebSocketsClient) {
                    contentConverter = KotlinxWebsocketSerializationConverter(Json)
                }
            }

            @Suppress("DeferredResultUnused")
            runBlocking {
                async {
                    client.webSocket("/tasks") {
                        val allTasks = receiveDeserialized<TaskResponseMessage>()
                        assertIs<TaskResponseMessage.AllTasks>(allTasks)
                        val createTaskRequest: TaskRequestMessage = TaskRequestMessage.CreateNewTask(
                            Task(
                                title = "Title",
                                description = "Desc",
                                status = TaskStatus.WorkInProgress,
                            )
                        )
                        sendSerialized(createTaskRequest)
                        val createdTask = receiveDeserialized<TaskResponseMessage>()
                        assertIs<TaskResponseMessage.CreatedTask>(createdTask)

                        val updateTaskRequest: TaskRequestMessage = TaskRequestMessage.UpdateTask(
                            createdTask.task.copy(
                                title = "Updated title",
                                description = "Updated Description",
                                status = TaskStatus.Done,
                            )
                        )
                        sendSerialized(updateTaskRequest)
                        val updatedTask = receiveDeserialized<TaskResponseMessage>()
                        assertIs<TaskResponseMessage.UpdatedTask>(updatedTask)

                        val deleteTaskRequest: TaskRequestMessage = TaskRequestMessage.DeleteTask(createdTask.task.id)
                        sendSerialized(deleteTaskRequest)
                        val deletedTask = receiveDeserialized<TaskResponseMessage>()
                        assertIs<TaskResponseMessage.DeletedTask>(deletedTask)
                        assertTrue(incoming.isEmpty)
                        close()
                    }
                }
                async {
                    client.webSocket("/tasks") {
                        val allTasks = receiveDeserialized<TaskResponseMessage>()
                        assertIs<TaskResponseMessage.AllTasks>(allTasks)
                        val createdTask = receiveDeserialized<TaskResponseMessage>()
                        assertIs<TaskResponseMessage.CreatedTask>(createdTask)
                        val updatedTask = receiveDeserialized<TaskResponseMessage>()
                        assertIs<TaskResponseMessage.UpdatedTask>(updatedTask)
                        val deletedTask = receiveDeserialized<TaskResponseMessage>()
                        assertIs<TaskResponseMessage.DeletedTask>(deletedTask)
                        assertTrue(incoming.isEmpty)
                        close()
                    }
                }
            }
        }
}
