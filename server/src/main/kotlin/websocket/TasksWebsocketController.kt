package websocket

import io.ktor.server.routing.Routing
import io.ktor.server.websocket.receiveDeserialized
import io.ktor.server.websocket.sendSerialized
import io.ktor.server.websocket.webSocket
import io.ktor.websocket.close
import kotlinx.coroutines.channels.ClosedReceiveChannelException
import kotlinx.coroutines.isActive
import mu.KotlinLogging
import repository.TaskRepository
import task.TaskRequestMessage
import task.TaskResponseMessage

private val log = KotlinLogging.logger { }

fun Routing.tasksWebsocketController() {
    webSocket("/tasks") {
        val sessionId = WebsocketSessionManager.putSession(this)
        closeReason.invokeOnCompletion {
            log.info { "connection closed $sessionId" }
            WebsocketSessionManager.removeSession(sessionId)
        }

        log.info { "new connection $sessionId: $this" }

        val allTasks = TaskRepository.getAllTAsks()
        sendSerialized(TaskResponseMessage.AllTasks(allTasks) as TaskResponseMessage)

        while (isActive) {
            try {
                val request = receiveDeserialized<TaskRequestMessage>()
                log.info { "Received request: $request" }
                val responseMessage = when (request) {
                    is TaskRequestMessage.CreateNewTask -> {
                        val createdTask = TaskRepository.createTask(request.task)
                        TaskResponseMessage.CreatedTask(createdTask)
                    }

                    is TaskRequestMessage.UpdateTask -> {
                        val updatedTask = TaskRepository.updateTask(request.task)
                        TaskResponseMessage.UpdatedTask(updatedTask)
                    }

                    is TaskRequestMessage.DeleteTask -> {
                        TaskRepository.deleteTask(request.taskId)
                        TaskResponseMessage.DeletedTask(request.taskId)
                    }
                }
                WebsocketSessionManager.notifyAll(responseMessage)
            } catch (e: Exception) {
                if (e !is ClosedReceiveChannelException) {
                    log.error(e) { "Closing websocket session because of exception" }
                }
                close()
            }
        }
    }
}
