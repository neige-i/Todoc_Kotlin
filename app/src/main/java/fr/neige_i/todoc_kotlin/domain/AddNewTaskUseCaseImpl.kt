package fr.neige_i.todoc_kotlin.domain

import fr.neige_i.todoc_kotlin.data.ProjectRepositoryImpl
import fr.neige_i.todoc_kotlin.data.TaskRepositoryImpl
import fr.neige_i.todoc_kotlin.data.model.Task
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.time.Clock
import java.time.Instant
import javax.inject.Inject

class AddNewTaskUseCaseImpl @Inject constructor(
    private val taskRepository: TaskRepositoryImpl,
    private val projectRepository: ProjectRepositoryImpl,
    private val applicationScope: CoroutineScope,
    private val ioDispatcher: CoroutineDispatcher,
    private val defaultClock: Clock,
) : AddNewTaskUseCase {

    override fun invoke(projectId: Long, taskName: String) {
        applicationScope.launch(ioDispatcher) {

            projectRepository.getProject(projectId)?.let {
                taskRepository.addTask(Task(
                    // TODO: set projectId with "it" or with argument
                    projectId = it.id,
                    name = taskName,
                    creationTimestamp = Instant.now(defaultClock).toEpochMilli()
                ))
            }
        }
    }
}