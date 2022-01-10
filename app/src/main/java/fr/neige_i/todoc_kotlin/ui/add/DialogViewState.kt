package fr.neige_i.todoc_kotlin.ui.add

import fr.neige_i.todoc_kotlin.data.model.Project

data class DialogViewState(
    val projectList: List<Project>,
    val taskError: String?,
    val projectError: String?,
)
