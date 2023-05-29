package repository

import task.Task
import task.TaskStatus


object TaskRepository {
    private val tasks = mutableMapOf(
        1L to Task(1L, "Hello", "World", TaskStatus.Backlog),
        2L to Task(2L, "How", "are you?", TaskStatus.Backlog),
    )

    private var nextTaskId = tasks.values.maxOf { it.id } + 1L
        get() = field++

    fun getAllTAsks(): List<Task> {
        return tasks.values.toList()
    }

    fun createTask(task: Task): Task {
        check(task.id == 0L) { "Cannot create Task that has id ${task.id}" }
        val savedTask = task.copy(id = nextTaskId)
        tasks[savedTask.id] = savedTask
        return savedTask
    }

    fun updateTask(task: Task): Task {
        if (task.id !in tasks) error("Cannot update task with id ${task.id} because it does not exist")
        tasks[task.id] = task
        return task
    }

    fun deleteTask(taskId: Long) {
        if (taskId !in tasks) error("Cannot delete task with id $taskId because it does not exist")
        tasks -= taskId
    }
}
