import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import service.TaskService
import task.Task

@Composable
fun PlanifyClient() {
    val tasks = TaskService.tasks

    MaterialTheme {
        Column(modifier = Modifier.fillMaxSize()) {
            for (task in tasks) {
                Task(task)
            }
        }
    }
}

@Composable
fun Task(task: Task) {
    Box(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
        Column(modifier = Modifier.align(Alignment.TopStart), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(text = task.title)
            Text(text = task.description)
        }
        Column(modifier = Modifier.align(Alignment.TopEnd), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(text = task.id.toString())
            Text(text = task.status.name)
        }
    }
}
