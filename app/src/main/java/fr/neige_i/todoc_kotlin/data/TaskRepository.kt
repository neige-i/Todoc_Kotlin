package fr.neige_i.todoc_kotlin.data

import fr.neige_i.todoc_kotlin.data.model.Task
import kotlinx.coroutines.flow.Flow

interface TaskRepository {

    val allTasks: Flow<List<Task>>

    val tasksByNameAsc: Flow<List<Task>>
    val tasksByNameDesc: Flow<List<Task>>
    val tasksByProjectNameAsc: Flow<List<Task>>
    val tasksByProjectNameDesc: Flow<List<Task>>
    val tasksByDateAsc: Flow<List<Task>>
    val tasksByDateDesc: Flow<List<Task>>

    suspend fun addTask(task: Task)
    suspend fun deleteTask(taskId: Long)
}