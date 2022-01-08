package fr.neige_i.todoc_kotlin.ui.task_add

import fr.neige_i.todoc_kotlin.data.model.Project

data class AddDialogViewState(
    val projectList: List<Project>,
    val taskError: String?,
    val projectError: String?,
)
