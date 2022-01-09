package fr.neige_i.todoc_kotlin.ui.task_list

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import fr.neige_i.todoc_kotlin.domain.DeleteTaskUseCase
import fr.neige_i.todoc_kotlin.domain.GetTasksWithProjectsUseCase
import javax.inject.Inject

@HiltViewModel
class TaskListViewModel @Inject constructor(
    getTasksWithProjectsUseCase: GetTasksWithProjectsUseCase,
    private val deleteTaskUseCase: DeleteTaskUseCase,
) : ViewModel() {

    val viewState: LiveData<MainViewState> =
        Transformations.map(getTasksWithProjectsUseCase().asLiveData()) {
            MainViewState(
                it.map { entry ->
                    val (task, project) = entry
                    TaskViewState(
                        task.id,
                        task.name,
                        project.name,
                        project.color
                    )
                },
                it.isEmpty()
            )
        }

    fun onTaskRemoved(projectId: Long) {
        deleteTaskUseCase(projectId)
    }
}