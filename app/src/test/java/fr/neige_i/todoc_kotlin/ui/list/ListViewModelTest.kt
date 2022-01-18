package fr.neige_i.todoc_kotlin.ui.list

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import fr.neige_i.todoc_kotlin.data.model.Project
import fr.neige_i.todoc_kotlin.data.model.Task
import fr.neige_i.todoc_kotlin.domain.DeleteTaskUseCase
import fr.neige_i.todoc_kotlin.domain.GetAllTasksWithProjectsUseCase
import fr.neige_i.todoc_kotlin.util.TestLifecycleRule.getValueForTesting
import fr.neige_i.todoc_kotlin.util.TestCoroutineRule
import io.mockk.*
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class ListViewModelTest {

    companion object {
        // OUT
        private val project1 = Project(1, "project1", 0x1)
        private val project2 = Project(2, "project2", 0x2)
        private val project3 = Project(3, "project3", 0x3)

        private val task1_2 = Task(1, 2, "task1", 2)
        private val task2_2 = Task(2, 2, "task2", 4)
        private val task3_1 = Task(3, 1, "task3", 1)
        private val task4_3 = Task(4, 3, "task4", 3)
    }

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var listViewModel: ListViewModel

    @MockK
    private lateinit var getAllTasksWithProjectsUseCaseMock: GetAllTasksWithProjectsUseCase

    @MockK
    private lateinit var deleteTaskUseCaseMock: DeleteTaskUseCase

    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        coJustRun { deleteTaskUseCaseMock(any()) }

        listViewModel = ListViewModel(
            getAllTasksWithProjectsUseCase = getAllTasksWithProjectsUseCaseMock,
            deleteTaskUseCase = deleteTaskUseCaseMock,
            ioDispatcher = testCoroutineRule.testCoroutineDispatcher
        )
    }

    @Test
    fun `return non empty list`() {
        // GIVEN
        every { getAllTasksWithProjectsUseCaseMock(TaskSortingType.NONE) } returns flowOf(mapOf(
            task3_1 to project1,
            task4_3 to project3,
            task1_2 to project2,
            task2_2 to project2
        ))

        // WHEN
        val viewState = getValueForTesting(listViewModel.viewState)

        // THEN
        assertEquals(
            ListViewState(
                taskViewStates = listOf(
                    TaskViewState(task3_1.id, task3_1.name, project1.name, project1.color),
                    TaskViewState(task4_3.id, task4_3.name, project3.name, project3.color),
                    TaskViewState(task1_2.id, task1_2.name, project2.name, project2.color),
                    TaskViewState(task2_2.id, task2_2.name, project2.name, project2.color),
                ),
                emptyTaskTextVisibility = false
            ),
            viewState
        )

        verify(exactly = 1) { getAllTasksWithProjectsUseCaseMock.invoke(TaskSortingType.NONE) }
        confirmVerified(getAllTasksWithProjectsUseCaseMock, deleteTaskUseCaseMock)
    }

    @Test
    fun `return empty list`() {
        // GIVEN
        every { getAllTasksWithProjectsUseCaseMock(TaskSortingType.NONE) } returns flowOf(mapOf())

        // WHEN
        val viewState = getValueForTesting(listViewModel.viewState)

        // THEN
        assertEquals(
            ListViewState(
                taskViewStates = listOf(),
                emptyTaskTextVisibility = true
            ),
            viewState
        )

        verify(exactly = 1) { getAllTasksWithProjectsUseCaseMock.invoke(TaskSortingType.NONE) }
        confirmVerified(getAllTasksWithProjectsUseCaseMock, deleteTaskUseCaseMock)
    }

    @Test
    fun `make the UseCase remove the task`() {
        // WHEN
        listViewModel.onTaskRemoved(42)

        // THEN
        coVerify(exactly = 1) { deleteTaskUseCaseMock(42) }
        confirmVerified(getAllTasksWithProjectsUseCaseMock, deleteTaskUseCaseMock)
    }

    @Test
    fun `return true if item is a known sorting type`() {
        // WHEN
        val result = listViewModel.onMenuItemClicked(TaskSortingType.DATE_ASC.id)

        // THEN
        assertTrue(result)

        confirmVerified(getAllTasksWithProjectsUseCaseMock, deleteTaskUseCaseMock)
    }

    @Test
    fun `return false if item is an unknown sorting type`() {
        // WHEN
        val result = listViewModel.onMenuItemClicked(-1)

        // THEN
        assertFalse(result)

        confirmVerified(getAllTasksWithProjectsUseCaseMock, deleteTaskUseCaseMock)
    }
}