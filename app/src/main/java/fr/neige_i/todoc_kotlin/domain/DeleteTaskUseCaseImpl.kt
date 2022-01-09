package fr.neige_i.todoc_kotlin.domain

import fr.neige_i.todoc_kotlin.data.TaskRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject

class DeleteTaskUseCaseImpl @Inject constructor(
    private val taskRepository: TaskRepository,
    private val applicationScope: CoroutineScope,
    private val ioDispatcher: CoroutineDispatcher,
) : DeleteTaskUseCase {

    override fun invoke(projectId: Long) {
        // TODO: which scope is preferred for deleting a task
        applicationScope.launch(ioDispatcher) {

            taskRepository.deleteTask(projectId)
        }
    }
}