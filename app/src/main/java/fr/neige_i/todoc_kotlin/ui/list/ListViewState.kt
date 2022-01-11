package fr.neige_i.todoc_kotlin.ui.list

data class ListViewState(
    val taskViewStates: List<TaskViewState>,
    val emptyTaskTextVisibility: Boolean,
)
