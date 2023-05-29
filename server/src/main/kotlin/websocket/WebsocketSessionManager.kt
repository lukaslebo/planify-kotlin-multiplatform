package websocket

import io.ktor.server.websocket.WebSocketServerSession
import io.ktor.server.websocket.sendSerialized
import task.TaskResponseMessage


object WebsocketSessionManager {
    private val sessionsBySessionId = mutableMapOf<Long, WebSocketServerSession>()
    private var nextSessionId = 1L
        get() = field++

    fun putSession(session: WebSocketServerSession): Long {
        val id = nextSessionId
        sessionsBySessionId[id] = session
        return id
    }

    fun removeSession(sessionId: Long) {
        sessionsBySessionId -= sessionId
    }

    suspend fun notifyAll(message: TaskResponseMessage) {
        for (it in sessionsBySessionId.values) {
            it.sendSerialized(message)
        }
    }
}
