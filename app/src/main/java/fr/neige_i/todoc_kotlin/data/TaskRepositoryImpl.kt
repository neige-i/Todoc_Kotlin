package fr.neige_i.todoc_kotlin.data

import fr.neige_i.todoc_kotlin.data.data_source.TaskDao
import fr.neige_i.todoc_kotlin.data.model.Task
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class TaskRepositoryImpl @Inject constructor(private val taskDao: TaskDao) : TaskRepository {

    override fun getTask(taskId: Long): Flow<Task> = taskDao.getTaskById(taskId)
    override val allTasks: Flow<List<Task>> = taskDao.getAllTasks()

    override val tasksByNameAsc: Flow<List<Task>> = taskDao.getTasksByNameAsc()
    override val tasksByNameDesc: Flow<List<Task>> = taskDao.getTasksByNameDesc()
    override val tasksByProjectNameAsc: Flow<List<Task>> = taskDao.getTasksByProjectNameAsc()
    override val tasksByProjectNameDesc: Flow<List<Task>> = taskDao.getTasksByProjectNameDesc()
    override val tasksByDateAsc: Flow<List<Task>> = taskDao.getTasksByDateAsc()
    override val tasksByDateDesc: Flow<List<Task>> = taskDao.getTasksByDateDesc()

    override suspend fun addTask(task: Task) = taskDao.insert(task)
    override suspend fun deleteTask(taskId: Long) = taskDao.deleteWithId(taskId)
}