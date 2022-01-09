package fr.neige_i.todoc_kotlin.domain

import fr.neige_i.todoc_kotlin.data.ProjectRepository
import fr.neige_i.todoc_kotlin.data.TaskRepository
import fr.neige_i.todoc_kotlin.data.model.Project
import fr.neige_i.todoc_kotlin.data.model.Task
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

class GetTasksWithProjectsUseCaseImpl @Inject constructor(
    private val taskRepository: TaskRepository,
    private val projectRepository: ProjectRepository,
) : GetTasksWithProjectsUseCase {

    override fun invoke(): Flow<Map<Task, Project>> =
        combine(taskRepository.allTasks, projectRepository.allProjects) { taskList, projectList ->

            taskList.associateWith { task ->
                projectList.first { project -> project.id == task.projectId }
            }
        }
}