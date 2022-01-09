package fr.neige_i.todoc_kotlin.domain

interface DeleteTaskUseCase {

    operator fun invoke(projectId: Long)
}