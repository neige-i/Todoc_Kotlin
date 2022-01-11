package fr.neige_i.todoc_kotlin.data.repository

import fr.neige_i.todoc_kotlin.data.data_source.ProjectDao
import fr.neige_i.todoc_kotlin.data.model.Project
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ProjectRepositoryImpl @Inject constructor(
    private val projectDao: ProjectDao,
) : ProjectRepository {

    override fun getAllProjects(): Flow<List<Project>> = projectDao.getAllProjects()
}