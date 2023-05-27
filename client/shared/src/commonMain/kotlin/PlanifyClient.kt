import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun PlanifyClient() {
    MaterialTheme {
        Text(
            modifier = Modifier.padding(50.dp),
            text = HelloPlanify.greeting,
        )
    }
}
