package fr.neige_i.todoc_kotlin.ui.task_list

data class MainViewState(
    val taskViewStates: List<TaskViewState>,
    val emptyTaskTextVisibility: Int, // TODO: rather use Boolean type
)
