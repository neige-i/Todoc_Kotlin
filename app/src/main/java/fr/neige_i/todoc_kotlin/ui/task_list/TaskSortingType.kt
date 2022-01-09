package fr.neige_i.todoc_kotlin.ui.task_list

import fr.neige_i.todoc_kotlin.R

enum class TaskSortingType(val id: Int) {
    TASK_NAME_ASC(R.id.sort_alphabetical_task),
    TASK_NAME_DESC(R.id.sort_alphabetical_inverted_task),
    PROJECT_NAME_ASC(R.id.sort_alphabetical_project),
    PROJECT_NAME_DESC(R.id.sort_alphabetical_inverted_project),
    DATE_ASC(R.id.sort_oldest_first),
    DATE_DESC(R.id.sort_recent_first),
    NONE(0),
}