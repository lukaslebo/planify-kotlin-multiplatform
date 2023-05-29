package task

import kotlinx.serialization.Serializable

@Serializable
data class Task(
    val id: Long = 0L,
    val title: String,
    val description: String,
    val status: TaskStatus,
)

enum class TaskStatus {
    Backlog,
    Todo,
    WorkInProgress,
    Done,
    Closed,
}

@Serializable
sealed interface TaskRequestMessage {
    @Serializable
    data class CreateNewTask(val task: Task) : TaskRequestMessage

    @Serializable
    data class UpdateTask(val task: Task) : TaskRequestMessage

    @Serializable
    data class DeleteTask(val taskId: Long) : TaskRequestMessage
}


@Serializable
sealed interface TaskResponseMessage {
    @Serializable
    data class AllTasks(val tasks: List<Task>) : TaskResponseMessage

    @Serializable
    data class CreatedTask(val task: Task) : TaskResponseMessage

    @Serializable
    data class UpdatedTask(val task: Task) : TaskResponseMessage

    @Serializable
    data class DeletedTask(val taskId: Long) : TaskResponseMessage
}

