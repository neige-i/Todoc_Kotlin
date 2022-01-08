package fr.neige_i.todoc_kotlin.domain

interface AddNewTaskUseCase {

    operator fun invoke(projectId: Long, taskName: String)
}