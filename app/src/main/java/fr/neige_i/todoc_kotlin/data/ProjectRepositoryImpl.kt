package fr.neige_i.todoc_kotlin.data

import fr.neige_i.todoc_kotlin.data.data_source.ProjectDao
import fr.neige_i.todoc_kotlin.data.model.Project
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ProjectRepositoryImpl @Inject constructor(
    private val projectDao: ProjectDao,
) : ProjectRepository {

    override val allProjects: Flow<List<Project>> = projectDao.getAllProjects()

    override suspend fun getProject(projectId: Long): Project? =
        projectDao.getProjectById(projectId)
}