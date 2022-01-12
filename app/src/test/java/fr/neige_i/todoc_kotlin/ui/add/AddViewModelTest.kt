package fr.neige_i.todoc_kotlin.ui.add

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import fr.neige_i.todoc_kotlin.R
import fr.neige_i.todoc_kotlin.data.model.Project
import fr.neige_i.todoc_kotlin.domain.AddNewTaskUseCase
import fr.neige_i.todoc_kotlin.domain.GetAllProjectsUseCase
import fr.neige_i.todoc_kotlin.util.TestLifecycleRule.getLiveDataTriggerCount
import fr.neige_i.todoc_kotlin.util.TestLifecycleRule.getValueForTesting
import fr.neige_i.todoc_kotlin.util.TestCoroutineRule
import io.mockk.*
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
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
        coEvery { addNewTaskUseCaseMock(project3.id, TASK_NAME_TO_ADD) } returns Unit

        addViewModel = AddViewModel(
            getAllProjectsUseCaseMock,
            addNewTaskUseCaseMock,
            testCoroutineRule.testCoroutineDispatcher,
            testCoroutineRule.testCoroutineDispatcher,
            contextMockK
        )
    }

    @Test
    fun `returns initial view state when ViewModel is created`() {
        // WHEN
        val viewState = getValueForTesting(addViewModel.viewStateLiveData)

        // THEN
        assertEquals(
            DialogViewState(
                getDefaultProjectList(),
                null,
                null
            ),
            viewState
        )
    }

    @Test
    fun `display BOTH task and project errors when the button is clicked with empty fields`() {
        // WHEN
        addViewModel.onPositiveButtonClicked()
        val viewState = getValueForTesting(addViewModel.viewStateLiveData)
        val dismissEvent = getLiveDataTriggerCount(addViewModel.dismissDialogLiveData)

        // THEN
        assertEquals(
            DialogViewState(
                getDefaultProjectList(),
                TASK_ERROR,
                PROJECT_ERROR
            ),
            viewState
        )
        assertEquals(0, dismissEvent) // Dialog is not dismissed

        coVerify(exactly = 0) { addNewTaskUseCaseMock(any(), any()) } // Task is not added
        confirmVerified(addNewTaskUseCaseMock)
    }

    @Test
    fun `display TASK ERROR only when the button is clicked with empty task but project is selected`() {
        // GIVEN
        addViewModel.onProjectSelected(project2)

        // WHEN
        addViewModel.onPositiveButtonClicked()
        val viewState = getValueForTesting(addViewModel.viewStateLiveData)
        val dismissEvent = getLiveDataTriggerCount(addViewModel.dismissDialogLiveData)

        // THEN
        assertEquals(
            DialogViewState(
                getDefaultProjectList(),
                TASK_ERROR,
                null // No error, because the project #2 has been selected
            ),
            viewState
        )
        assertEquals(0, dismissEvent) // Dialog is not dismissed

        coVerify(exactly = 0) { addNewTaskUseCaseMock(any(), any()) } // Task is not added
        confirmVerified(addNewTaskUseCaseMock)
    }

    @Test
    fun `display TASK ERROR only when the button is clicked with null task but project is selected`() {
        // GIVEN
        addViewModel.afterTaskNameChanged("null")
        addViewModel.onProjectSelected(project2)

        // WHEN
        addViewModel.onPositiveButtonClicked()
        val viewState = getValueForTesting(addViewModel.viewStateLiveData)
        val dismissEvent = getLiveDataTriggerCount(addViewModel.dismissDialogLiveData)

        // THEN
        assertEquals(
            DialogViewState(
                getDefaultProjectList(),
                TASK_ERROR,
                null // No error, because the project #2 has been selected
            ),
            viewState
        )
        assertEquals(0, dismissEvent) // Dialog is not dismissed

        coVerify(exactly = 0) { addNewTaskUseCaseMock(any(), any()) } // Task is not added
        confirmVerified(addNewTaskUseCaseMock)
    }

    @Test
    fun `display PROJECT ERROR only when the button is clicked with empty project but task is typed`() {
        // GIVEN
        addViewModel.afterTaskNameChanged(TASK_NAME_TO_ADD)

        // WHEN
        addViewModel.onPositiveButtonClicked()
        val viewState = getValueForTesting(addViewModel.viewStateLiveData)
        val dismissEvent = getLiveDataTriggerCount(addViewModel.dismissDialogLiveData)

        // THEN
        assertEquals(
            DialogViewState(
                getDefaultProjectList(),
                null, // No error, because the project #2 has been selected
                PROJECT_ERROR
            ),
            viewState
        )
        assertEquals(0, dismissEvent) // Dialog is not dismissed

        coVerify(exactly = 0) { addNewTaskUseCaseMock(any(), any()) } // Task is not added
        confirmVerified(addNewTaskUseCaseMock)
    }

    @Test
    fun `display NO ERRORS, DISMISS the dialog and ADD the task when the button is clicked with correct fields`() = runTest {
        // GIVEN
        addViewModel.afterTaskNameChanged(TASK_NAME_TO_ADD)
        addViewModel.onProjectSelected(project3)

        // WHEN
        addViewModel.onPositiveButtonClicked()
        val viewState = getValueForTesting(addViewModel.viewStateLiveData)
        val dismissEvent = getLiveDataTriggerCount(addViewModel.dismissDialogLiveData)

        // THEN
        assertEquals(
            DialogViewState(
                getDefaultProjectList(),
                null,
                null
            ),
            viewState
        )
        assertEquals(1, dismissEvent) // Dialog IS dismissed

        coVerify(exactly = 1) { addNewTaskUseCaseMock(project3.id, TASK_NAME_TO_ADD) } // Task IS added
        confirmVerified(addNewTaskUseCaseMock)
    }

    // region out

    private fun getDefaultProjectList() = listOf(project1, project2, project3)

    // endregion out
}