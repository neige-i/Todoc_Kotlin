package fr.neige_i.todoc_kotlin.ui.list

import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import fr.neige_i.todoc_kotlin.di.IoCoroutineDispatcher
import fr.neige_i.todoc_kotlin.domain.DeleteTaskUseCase
import fr.neige_i.todoc_kotlin.domain.GetAllTasksWithProjectsUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ListViewModel @Inject constructor(
    private val getAllTasksWithProjectsUseCase: GetAllTasksWithProjectsUseCase,
    private val deleteTaskUseCase: DeleteTaskUseCase,
    @IoCoroutineDispatcher private val ioDispatcher: CoroutineDispatcher,
) : ViewModel() {

    // Init default sorting type to "none"
    private val sortingMutableLiveData = MutableLiveData(TaskSortingType.NONE)

    val viewState: LiveData<ListViewState> =
        Transformations.switchMap(sortingMutableLiveData) { sortingType ->

            liveData<ListViewState> {
                getAllTasksWithProjectsUseCase(sortingType).collect {
                    emit(
                        ListViewState(
                            taskViewStates = it.map { entry ->
                                val (task, project) = entry
                                TaskViewState(
                                    task.id,
                                    task.name,
                                    project.name,
                                    project.color
                                )
                            },
                            emptyTaskTextVisibility = it.isEmpty()
                        )
                    )
                }
            }
        }

    fun onTaskRemoved(projectId: Long) {
        viewModelScope.launch(ioDispatcher) { deleteTaskUseCase(projectId) }
    }

    fun onMenuItemClicked(itemId: Int): Boolean {
        val sortingType = TaskSortingType.values().firstOrNull { it.id == itemId }

        sortingType?.let { sortingMutableLiveData.value = it }

        return sortingType != null
    }
}
