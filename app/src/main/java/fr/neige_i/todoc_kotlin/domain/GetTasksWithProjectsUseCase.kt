package fr.neige_i.todoc_kotlin.domain

import fr.neige_i.todoc_kotlin.data.model.Project
import fr.neige_i.todoc_kotlin.data.model.Task
import kotlinx.coroutines.flow.Flow

interface GetTasksWithProjectsUseCase {

    operator fun invoke(): Flow<Map<Task, Project>>
}