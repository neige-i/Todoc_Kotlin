package fr.neige_i.todoc_kotlin.domain

import fr.neige_i.todoc_kotlin.data.model.Task
import fr.neige_i.todoc_kotlin.data.repository.TaskRepository
import fr.neige_i.todoc_kotlin.di.IoCoroutineDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import java.time.Clock
import javax.inject.Inject

class AddNewTaskUseCaseImpl @Inject constructor(
    private val taskRepository: TaskRepository,
    @IoCoroutineDispatcher private val ioDispatcher: CoroutineDispatcher,
    private val defaultClock: Clock,
) : AddNewTaskUseCase {

    // withContext() is added for testing purpose
    override suspend fun invoke(projectId: Long, taskName: String) = withContext(ioDispatcher) {
        taskRepository.addTask(Task(
            projectId = projectId,
            name = taskName,
            creationTimestamp = defaultClock.millis()
        ))
    }
}