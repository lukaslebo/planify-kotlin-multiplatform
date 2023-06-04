import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import service.TaskService

fun main() = application {
    TaskService.startAutoReconnect()
    Window(onCloseRequest = {
        TaskService.closeConnection()
        exitApplication()
    }) {
        PlanifyClient()
    }
}
