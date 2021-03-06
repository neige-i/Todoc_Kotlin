package fr.neige_i.todoc_kotlin.data.repository

import fr.neige_i.todoc_kotlin.data.model.Task
import kotlinx.coroutines.flow.Flow

interface TaskRepository {

    fun getTask(taskId: Long): Flow<Task>
    fun getAllTasks(): Flow<List<Task>>

    fun getTasksByNameAsc(): Flow<List<Task>>
    fun getTasksByNameDesc(): Flow<List<Task>>
    fun getTasksByProjectNameAsc(): Flow<List<Task>>
    fun getTasksByProjectNameDesc(): Flow<List<Task>>
    fun getTasksByDateAsc(): Flow<List<Task>>
    fun getTasksByDateDesc(): Flow<List<Task>>

    suspend fun addTask(task: Task)
    suspend fun deleteTask(taskId: Long)
}