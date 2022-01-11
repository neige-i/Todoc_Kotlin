package fr.neige_i.todoc_kotlin.domain

interface AddNewTaskUseCase {

    suspend operator fun invoke(projectId: Long, taskName: String)
}