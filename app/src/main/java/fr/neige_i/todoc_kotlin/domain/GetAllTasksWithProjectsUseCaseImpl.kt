package fr.neige_i.todoc_kotlin.domain

import fr.neige_i.todoc_kotlin.data.repository.ProjectRepository
import fr.neige_i.todoc_kotlin.data.repository.TaskRepository
import fr.neige_i.todoc_kotlin.data.model.Project
import fr.neige_i.todoc_kotlin.data.model.Task
import fr.neige_i.todoc_kotlin.ui.list.TaskSortingType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

class GetAllTasksWithProjectsUseCaseImpl @Inject constructor(
    private val taskRepository: TaskRepository,
    private val projectRepository: ProjectRepository,
) : GetAllTasksWithProjectsUseCase {

    override fun invoke(taskSortingType: TaskSortingType): Flow<Map<Task, Project>> =
        combine(getSortedTasks(taskSortingType), projectRepository.allProjects) { tasks, projects ->

            tasks.associateWith { task ->
                projects.first { project -> project.id == task.projectId }
            }
        }

    private fun getSortedTasks(taskSortingType: TaskSortingType): Flow<List<Task>> =
        when (taskSortingType) {
            TaskSortingType.TASK_NAME_ASC -> taskRepository.tasksByNameAsc
            TaskSortingType.TASK_NAME_DESC -> taskRepository.tasksByNameDesc
            TaskSortingType.PROJECT_NAME_ASC -> taskRepository.tasksByProjectNameAsc
            TaskSortingType.PROJECT_NAME_DESC -> taskRepository.tasksByProjectNameDesc
            TaskSortingType.DATE_ASC -> taskRepository.tasksByDateAsc
            TaskSortingType.DATE_DESC -> taskRepository.tasksByDateDesc
            TaskSortingType.NONE -> taskRepository.allTasks
        }
}