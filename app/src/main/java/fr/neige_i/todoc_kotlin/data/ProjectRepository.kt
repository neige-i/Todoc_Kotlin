package fr.neige_i.todoc_kotlin.data

import fr.neige_i.todoc_kotlin.data.model.Project
import kotlinx.coroutines.flow.Flow

interface ProjectRepository {

    val allProjects: Flow<List<Project>>

    suspend fun getProject(projectId: Long): Project?
}