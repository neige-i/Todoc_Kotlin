package fr.neige_i.todoc_kotlin.domain

import fr.neige_i.todoc_kotlin.data.repository.ProjectRepository
import fr.neige_i.todoc_kotlin.data.repository.TaskRepository
import fr.neige_i.todoc_kotlin.data.model.Project
import fr.neige_i.todoc_kotlin.data.model.Task
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

class GetSingleTaskWithProjectUseCaseImpl @Inject constructor(
    private val taskRepository: TaskRepository,
    private val projectRepository: ProjectRepository,
) : GetSingleTaskWithProjectUseCase {

    override fun invoke(taskId: Long): Flow<Pair<Task, Project>> =
        combine(taskRepository.getTask(taskId), projectRepository.allProjects) { task, projects ->

            Pair(task, projects.first { project -> project.id == task.projectId })
        }
}