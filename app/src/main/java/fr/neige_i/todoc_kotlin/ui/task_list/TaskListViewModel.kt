package fr.neige_i.todoc_kotlin.ui.task_list

import android.view.View
import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import fr.neige_i.todoc_kotlin.domain.DeleteTaskUseCase
import fr.neige_i.todoc_kotlin.domain.GetTasksWithProjectsUseCase
import javax.inject.Inject

@HiltViewModel
class TaskListViewModel @Inject constructor(
    private val getTasksWithProjectsUseCase: GetTasksWithProjectsUseCase,
    private val deleteTaskUseCase: DeleteTaskUseCase,
) : ViewModel() {

    // Init default sorting type to "none"
    private val sortingMutableLiveData = MutableLiveData(TaskSortingType.NONE)

    // TODO: Transformations of transformations... mmh
    val viewState: LiveData<MainViewState> =
        Transformations.switchMap(sortingMutableLiveData) { sortingType ->
            Transformations.map(getTasksWithProjectsUseCase(sortingType).asLiveData()) { map ->
                MainViewState(
                    map.map { entry ->
                        val (task, project) = entry
                        TaskViewState(
                            task.id,
                            task.name,
                            project.name,
                            project.color
                        )
                    },
                    if (map.isEmpty()) View.VISIBLE else View.GONE
                )
            }
        }

    fun onTaskRemoved(projectId: Long) {
        deleteTaskUseCase(projectId)
    }

    // TODO: is "readable"
    fun onMenuItemClicked(itemId: Int): Boolean = TaskSortingType.values().firstOrNull {
        it.id == itemId
    }?.let {
        sortingMutableLiveData.value = it
        true
    } ?: false
}
