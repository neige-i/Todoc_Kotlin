package fr.neige_i.todoc_kotlin.domain

interface DeleteTaskUseCase {

    suspend operator fun invoke(projectId: Long)
}