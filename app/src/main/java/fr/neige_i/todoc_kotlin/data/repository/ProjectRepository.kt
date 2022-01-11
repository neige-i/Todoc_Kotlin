package fr.neige_i.todoc_kotlin.data.repository

import fr.neige_i.todoc_kotlin.data.model.Project
import kotlinx.coroutines.flow.Flow

interface ProjectRepository {

    fun getAllProjects(): Flow<List<Project>>
}