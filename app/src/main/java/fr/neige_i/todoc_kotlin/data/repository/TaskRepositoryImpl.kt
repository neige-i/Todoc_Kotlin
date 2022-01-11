package fr.neige_i.todoc_kotlin.data.repository

import fr.neige_i.todoc_kotlin.data.data_source.TaskDao
import fr.neige_i.todoc_kotlin.data.model.Task
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class TaskRepositoryImpl @Inject constructor(private val taskDao: TaskDao) : TaskRepository {

    override fun getTask(taskId: Long): Flow<Task> = taskDao.getTaskById(taskId)
    override fun getAllTasks(): Flow<List<Task>> = taskDao.getAllTasks()

    override fun getTasksByNameAsc(): Flow<List<Task>> = taskDao.getTasksByNameAsc()
    override fun getTasksByNameDesc(): Flow<List<Task>> = taskDao.getTasksByNameDesc()
    override fun getTasksByProjectNameAsc(): Flow<List<Task>> = taskDao.getTasksByProjectNameAsc()
    override fun getTasksByProjectNameDesc(): Flow<List<Task>> = taskDao.getTasksByProjectNameDesc()
    override fun getTasksByDateAsc(): Flow<List<Task>> = taskDao.getTasksByDateAsc()
    override fun getTasksByDateDesc(): Flow<List<Task>> = taskDao.getTasksByDateDesc()

    override suspend fun addTask(task: Task) = taskDao.insert(task)
    override suspend fun deleteTask(taskId: Long) = taskDao.deleteWithId(taskId)
}