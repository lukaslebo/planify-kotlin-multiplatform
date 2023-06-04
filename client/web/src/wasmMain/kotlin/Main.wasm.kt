import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.CanvasBasedWindow
import service.TaskService

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    TaskService.startAutoReconnect()
    CanvasBasedWindow("Planify") {
        PlanifyClient()
    }
}

