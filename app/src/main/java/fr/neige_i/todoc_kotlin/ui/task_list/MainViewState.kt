package fr.neige_i.todoc_kotlin.ui.task_list

data class MainViewState(
    val taskViewStates: List<TaskViewState>,
    val isNoTaskTextVisible: Boolean,
)
