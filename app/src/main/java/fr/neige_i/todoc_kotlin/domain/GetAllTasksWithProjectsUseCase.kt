package fr.neige_i.todoc_kotlin.domain

import fr.neige_i.todoc_kotlin.data.model.Project
import fr.neige_i.todoc_kotlin.data.model.Task
import fr.neige_i.todoc_kotlin.ui.list.TaskSortingType
import kotlinx.coroutines.flow.Flow

interface GetAllTasksWithProjectsUseCase {

    operator fun invoke(taskSortingType: TaskSortingType): Flow<Map<Task, Project>>
}