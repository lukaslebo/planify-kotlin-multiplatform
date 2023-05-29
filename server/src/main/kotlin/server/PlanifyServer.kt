package server

import io.ktor.serialization.kotlinx.KotlinxWebsocketSerializationConverter
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.plugins.cors.routing.CORS
import io.ktor.server.routing.routing
import io.ktor.server.websocket.WebSockets
import io.ktor.server.websocket.pingPeriod
import io.ktor.server.websocket.timeout
import kotlinx.serialization.json.Json
import websocket.tasksWebsocketController
import java.time.Duration

fun main() {
    embeddedServer(
        Netty,
        port = 8443,
        module = Application::planifyServerModule
    ).start(wait = true)
}

fun Application.planifyServerModule() {
    install(WebSockets) {
        contentConverter = KotlinxWebsocketSerializationConverter(Json)
        pingPeriod = Duration.ofSeconds(15)
        timeout = Duration.ofSeconds(15)
    }
    install(CORS) {
        anyHost()
    }
    install(ContentNegotiation) {
        json()
    }
    routing {
        tasksWebsocketController()
    }
}
