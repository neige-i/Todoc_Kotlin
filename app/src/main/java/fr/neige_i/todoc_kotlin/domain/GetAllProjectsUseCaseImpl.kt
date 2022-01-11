package fr.neige_i.todoc_kotlin.domain

import fr.neige_i.todoc_kotlin.data.repository.ProjectRepositoryImpl
import fr.neige_i.todoc_kotlin.data.model.Project
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllProjectsUseCaseImpl @Inject constructor(
    private val projectRepository: ProjectRepositoryImpl,
) : GetAllProjectsUseCase {

    override fun invoke(): Flow<List<Project>> = projectRepository.getAllProjects()
}