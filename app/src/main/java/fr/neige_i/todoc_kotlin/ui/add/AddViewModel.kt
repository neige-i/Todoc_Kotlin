package fr.neige_i.todoc_kotlin.ui.add

import android.app.Application
import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import fr.neige_i.todoc_kotlin.R
import fr.neige_i.todoc_kotlin.data.model.Project
import fr.neige_i.todoc_kotlin.di.IoCoroutineDispatcher
import fr.neige_i.todoc_kotlin.di.MainCoroutineDispatcher
import fr.neige_i.todoc_kotlin.domain.AddNewTaskUseCase
import fr.neige_i.todoc_kotlin.domain.GetAllProjectsUseCase
import fr.neige_i.todoc_kotlin.ui.util.SingleLiveEvent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class AddViewModel @Inject constructor(
    getAllProjectsUseCase: GetAllProjectsUseCase,
    private val addNewTaskUseCase: AddNewTaskUseCase,
    @IoCoroutineDispatcher private val ioDispatcher: CoroutineDispatcher,
    @MainCoroutineDispatcher private val mainDispatcher: CoroutineDispatcher,
    private val applicationContext: Application,
) : ViewModel() {

    private val viewStateMediatorLiveData = MediatorLiveData<DialogViewState>()
    val viewStateLiveData: LiveData<DialogViewState> = viewStateMediatorLiveData

    private val dismissDialogSingleLiveEvent = SingleLiveEvent<Unit>()
    val dismissDialogLiveData: LiveData<Unit> = dismissDialogSingleLiveEvent

    private val addButtonPingMutableLiveData = MutableLiveData<Boolean>()
    private var addTaskForm = AddTaskForm(
        taskName = "",
        taskError = null,
        selectedProject = null,
        projectError = null
    )

    init {
        val databaseProjectsMutableLiveData = getAllProjectsUseCase().asLiveData()

        viewStateMediatorLiveData.addSource(databaseProjectsMutableLiveData) { projectList ->
            viewStateCombine(projectList, addButtonPingMutableLiveData.value)
        }
        viewStateMediatorLiveData.addSource(addButtonPingMutableLiveData) { addButtonPing ->
            viewStateCombine(databaseProjectsMutableLiveData.value, addButtonPing)
        }
    }

    private fun viewStateCombine(projectList: List<Project>?, addButtonPing: Boolean?) {
        if (projectList == null || addButtonPing == false) {
            return
        }

        if (addButtonPing == true) {
            addButtonPingMutableLiveData.value = false

            updateFormErrors()
            handleButtonEvent()
        }

        viewStateMediatorLiveData.value = DialogViewState(
            projectList,
            addTaskForm.taskError,
            addTaskForm.projectError
        )
    }

    private fun updateFormErrors() {
        addTaskForm = addTaskForm.copy(
            taskError = if (addTaskForm.taskName.isNotBlank() && addTaskForm.taskName != "null") {
                null
            } else {
                applicationContext.getString(R.string.empty_task_name)
            },
            projectError = if (addTaskForm.selectedProject != null) {
                null
            } else {
                applicationContext.getString(R.string.empty_project)
            }
        )
    }

    private fun handleButtonEvent() {
        val capturedProject = addTaskForm.selectedProject

        if (addTaskForm.taskError == null &&
            addTaskForm.projectError == null &&
            capturedProject != null
        ) {
            viewModelScope.launch(ioDispatcher) {
                addNewTaskUseCase(capturedProject.id, addTaskForm.taskName)

                /* Jump to Main thread to set LiveDate with setValue() (here through call() method)
                to avoid calling postValue() from a background thread */
                withContext(mainDispatcher) {
                    dismissDialogSingleLiveEvent.call()
                }
            }
        }
    }

    fun afterTaskNameChanged(taskName: String?) {
        if (taskName != null) {
            addTaskForm = addTaskForm.copy(taskName = taskName)
        }
    }

    fun onProjectSelected(selectedProject: Project) {
        addTaskForm = addTaskForm.copy(selectedProject = selectedProject)
    }

    fun onPositiveButtonClicked() {
        addButtonPingMutableLiveData.value = true
    }

    private data class AddTaskForm(
        val taskName: String,
        val taskError: String?,
        val selectedProject: Project?,
        val projectError: String?,
    )
}