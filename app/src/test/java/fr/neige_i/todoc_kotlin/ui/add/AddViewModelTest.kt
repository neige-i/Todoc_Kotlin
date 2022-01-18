package fr.neige_i.todoc_kotlin.ui.add

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import fr.neige_i.todoc_kotlin.R
import fr.neige_i.todoc_kotlin.data.model.Project
import fr.neige_i.todoc_kotlin.domain.AddNewTaskUseCase
import fr.neige_i.todoc_kotlin.domain.GetAllProjectsUseCase
import fr.neige_i.todoc_kotlin.util.TestCoroutineRule
import fr.neige_i.todoc_kotlin.util.TestLifecycleRule.getValueForTesting
import fr.neige_i.todoc_kotlin.util.TestLifecycleRule.isLiveDataTriggered
import io.mockk.*
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class AddViewModelTest {

    companion object {
        // IN
        private const val TASK_NAME_TO_ADD = "new task"

        // OUT
        private const val TASK_ERROR = "task error"
        private const val PROJECT_ERROR = "project error"

        private val project1 = Project(1, "project1", 0x1)
        private val project2 = Project(2, "project2", 0x2)
        private val project3 = Project(3, "project3", 0x3)
    }

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var addViewModel: AddViewModel

    @MockK
    private lateinit var getAllProjectsUseCaseMock: GetAllProjectsUseCase

    @MockK
    private lateinit var addNewTaskUseCaseMock: AddNewTaskUseCase

    @MockK
    private lateinit var contextMockK: Application

    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        every { getAllProjectsUseCaseMock() } returns flowOf(getDefaultProjectList())
        every { contextMockK.getString(R.string.empty_task_name) } returns TASK_ERROR
        every { contextMockK.getString(R.string.empty_project) } returns PROJECT_ERROR
        coJustRun { addNewTaskUseCaseMock(project3.id, TASK_NAME_TO_ADD) }

        addViewModel = AddViewModel(
            getAllProjectsUseCase = getAllProjectsUseCaseMock,
            addNewTaskUseCase = addNewTaskUseCaseMock,
            ioDispatcher = testCoroutineRule.testCoroutineDispatcher,
            mainDispatcher = testCoroutineRule.testCoroutineDispatcher,
            applicationContext = contextMockK
        )

        verify(exactly = 1) { getAllProjectsUseCaseMock.invoke() }
    }

    @Test
    fun `nominal case`() {
        // WHEN
        // ViewModel is initialized

        val viewState = getValueForTesting(addViewModel.viewStateLiveData)

        // THEN
        assertEquals(
            getDefaultDialogViewState(),
            viewState
        )

        confirmVerified(getAllProjectsUseCaseMock, addNewTaskUseCaseMock)
    }

    @Test
    fun `display BOTH errors when BOTH fields are empty`() {
        // WHEN
        addViewModel.onPositiveButtonClicked()

        val viewState = getValueForTesting(addViewModel.viewStateLiveData)
        val isDialogDismissed = isLiveDataTriggered(addViewModel.dismissDialogLiveData)

        // THEN
        assertEquals(
            getDefaultDialogViewState().copy(taskError = TASK_ERROR, projectError = PROJECT_ERROR),
            viewState
        )
        assertFalse(isDialogDismissed)

        confirmVerified(getAllProjectsUseCaseMock, addNewTaskUseCaseMock)
    }

    @Test
    fun `display TASK error when project is selected`() {
        // GIVEN
        addViewModel.onProjectSelected(project2)

        // WHEN
        addViewModel.onPositiveButtonClicked()

        val viewState = getValueForTesting(addViewModel.viewStateLiveData)
        val isDialogDismissed = isLiveDataTriggered(addViewModel.dismissDialogLiveData)

        // THEN
        assertEquals(
            getDefaultDialogViewState().copy(taskError = TASK_ERROR),
            viewState
        )
        assertFalse(isDialogDismissed)

        confirmVerified(getAllProjectsUseCaseMock, addNewTaskUseCaseMock)
    }

    @Test
    fun `display PROJECT error when task is typed`() {
        // GIVEN
        addViewModel.afterTaskNameChanged(TASK_NAME_TO_ADD)

        // WHEN
        addViewModel.onPositiveButtonClicked()

        val viewState = getValueForTesting(addViewModel.viewStateLiveData)
        val isDialogDismissed = isLiveDataTriggered(addViewModel.dismissDialogLiveData)

        // THEN
        assertEquals(
            getDefaultDialogViewState().copy(projectError = PROJECT_ERROR),
            viewState
        )
        assertFalse(isDialogDismissed)

        confirmVerified(getAllProjectsUseCaseMock, addNewTaskUseCaseMock)
    }

    @Test
    fun `happy path`() = runTest {
        // GIVEN
        addViewModel.afterTaskNameChanged(TASK_NAME_TO_ADD)
        addViewModel.onProjectSelected(project3)

        // WHEN
        addViewModel.onPositiveButtonClicked()

        val viewState = getValueForTesting(addViewModel.viewStateLiveData)
        val isDialogDismissed = isLiveDataTriggered(addViewModel.dismissDialogLiveData)

        // THEN
        assertEquals(
            getDefaultDialogViewState(),
            viewState
        )
        assertTrue(isDialogDismissed)

        coVerify(exactly = 1) { addNewTaskUseCaseMock(project3.id, TASK_NAME_TO_ADD) }
        confirmVerified(getAllProjectsUseCaseMock, addNewTaskUseCaseMock)
    }

    // region out

    private fun getDefaultDialogViewState() = DialogViewState(
        projectList = getDefaultProjectList(),
        taskError = null,
        projectError = null
    )

    private fun getDefaultProjectList() = listOf(project1, project2, project3)

    // endregion out
}