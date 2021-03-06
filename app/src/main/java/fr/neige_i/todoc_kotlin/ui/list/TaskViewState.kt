package fr.neige_i.todoc_kotlin.ui.list

import androidx.annotation.ColorInt

data class TaskViewState(
    val taskId: Long,
    val taskName: String,
    val projectName: String,
    @ColorInt val projectColor: Int,
)
