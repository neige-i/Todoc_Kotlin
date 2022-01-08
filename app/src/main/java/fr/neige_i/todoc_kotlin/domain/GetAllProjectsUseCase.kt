package fr.neige_i.todoc_kotlin.domain

import fr.neige_i.todoc_kotlin.data.model.Project
import kotlinx.coroutines.flow.Flow

interface GetAllProjectsUseCase {

    operator fun invoke(): Flow<List<Project>>
}