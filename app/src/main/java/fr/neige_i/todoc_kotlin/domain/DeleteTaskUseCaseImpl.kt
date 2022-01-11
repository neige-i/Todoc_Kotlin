package fr.neige_i.todoc_kotlin.domain

import fr.neige_i.todoc_kotlin.data.repository.TaskRepository
import fr.neige_i.todoc_kotlin.di.IoCoroutineDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DeleteTaskUseCaseImpl @Inject constructor(
    private val taskRepository: TaskRepository,
    @IoCoroutineDispatcher private val ioDispatcher: CoroutineDispatcher,
) : DeleteTaskUseCase {

    // withContext() is added for testing purpose
    override suspend fun invoke(projectId: Long) = withContext(ioDispatcher) {
        taskRepository.deleteTask(projectId)
    }
}