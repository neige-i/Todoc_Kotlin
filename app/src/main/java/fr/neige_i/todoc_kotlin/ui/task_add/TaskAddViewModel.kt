package fr.neige_i.todoc_kotlin.ui.task_add

import android.app.Application
import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import fr.neige_i.todoc_kotlin.R
import fr.neige_i.todoc_kotlin.data.model.Project
import fr.neige_i.todoc_kotlin.domain.AddNewTaskUseCase
import fr.neige_i.todoc_kotlin.domain.GetAllProjectsUseCase
import fr.neige_i.todoc_kotlin.ui.util.SingleLiveEvent
import javax.inject.Inject

@HiltViewModel
class TaskAddViewModel @Inject constructor(
    getAllProjectsUseCase: GetAllProjectsUseCase,
    private val addNewTaskUseCase: AddNewTaskUseCase,
    private val applicationContext: Application,
) : ViewModel() {

    private val _viewState = MediatorLiveData<AddDialogViewState>()
    // TODO: rather use backing property
    val viewState: LiveData<AddDialogViewState> = _viewState

    private val _dismissDialogEvent = SingleLiveEvent<Unit>()
    val dismissDialogEvent: LiveData<Unit> = _dismissDialogEvent

    private val databaseProjectsMutableLiveData = getAllProjectsUseCase().asLiveData() // TODO: can be local
    private val addButtonPingMutableLiveData = MutableLiveData<Boolean>()
    // TODO: use late init instead of var? because of impossible smart cast
    private lateinit var typedTaskName: String
    private lateinit var selectedProject: Project

    init {
        _viewState.addSource(databaseProjectsMutableLiveData) { projectList ->
            viewStateCombine(projectList, addButtonPingMutableLiveData.value)
        }
        _viewState.addSource(addButtonPingMutableLiveData) { addButtonPing ->
            viewStateCombine(databaseProjectsMutableLiveData.value, addButtonPing)
        }
    }

    private fun viewStateCombine(projectList: List<Project>?, addButtonPing: Boolean?) {
        if (projectList == null || addButtonPing == false) {
            return
        }

        // TODO: is following code "readable"
        val (taskError, projectError) = if (addButtonPing == null) {
            // Initial value
            Pair(null, null)
        } else {
            this.addButtonPingMutableLiveData.value = false

            Pair(getTaskError(), getProjectError()).apply { handleButtonEvent(first, second) }
        }

        _viewState.value = AddDialogViewState(projectList, taskError, projectError)
    }

    private fun getTaskError(): String? = if (
        this::typedTaskName.isInitialized &&
        typedTaskName.isNotBlank() &&
        typedTaskName != "null"
    ) {
        null
    } else {
        applicationContext.getString(R.string.empty_task_name)
    }

    private fun getProjectError(): String? = if (this::selectedProject.isInitialized) {
        null
    } else {
        applicationContext.getString(R.string.empty_project)
    }

    private fun handleButtonEvent(taskError: String?, projectError: String?) {
        if (taskError == null && projectError == null) {
            _dismissDialogEvent.call()
            addNewTaskUseCase(selectedProject.id, typedTaskName)
        }
    }

    fun afterTaskNameChanged(taskName: String) {
        typedTaskName = taskName
    }

    fun onProjectSelected(selectedProject: Project) {
        this.selectedProject = selectedProject
    }

    fun onPositiveButtonClicked() {
        addButtonPingMutableLiveData.value = true
    }
}