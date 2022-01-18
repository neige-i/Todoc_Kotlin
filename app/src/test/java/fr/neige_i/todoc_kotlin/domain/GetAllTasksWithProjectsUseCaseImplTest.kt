package fr.neige_i.todoc_kotlin.domain

import fr.neige_i.todoc_kotlin.data.model.Project
import fr.neige_i.todoc_kotlin.data.model.Task
import fr.neige_i.todoc_kotlin.data.repository.ProjectRepository
import fr.neige_i.todoc_kotlin.data.repository.TaskRepository
import fr.neige_i.todoc_kotlin.ui.list.TaskSortingType
import fr.neige_i.todoc_kotlin.util.TestCoroutineRule
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class GetAllTasksWithProjectsUseCaseImplTest {

    companion object {
        // OUT
        private val project1 = Project(1, "project1", 0x1)
        private val project2 = Project(2, "project2", 0x2)
        private val project3 = Project(3, "project3", 0x3)

        private val task1_2_second = Task(1, 2, "task1", 2)
        private val task2_2_fourth = Task(2, 2, "task2", 4)
        private val task3_1_first = Task(3, 1, "task3", 1)
        private val task4_3_third = Task(4, 3, "task4", 3)
    }

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    private lateinit var getAllTasksWithProjectsUseCase: GetAllTasksWithProjectsUseCase

    @MockK
    private lateinit var taskRepositoryMock: TaskRepository

    @MockK
    private lateinit var projectRepositoryMock: ProjectRepository

    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        every { taskRepositoryMock.getTasksByNameAsc() } returns flowOf(
            listOf(task1_2_second, task2_2_fourth, task3_1_first, task4_3_third)
        )
        every { taskRepositoryMock.getTasksByNameDesc() } returns flowOf(
            listOf(task4_3_third, task3_1_first, task2_2_fourth, task1_2_second)
        )
        every { taskRepositoryMock.getTasksByProjectNameAsc() } returns flowOf(
            listOf(task3_1_first, task1_2_second, task2_2_fourth, task4_3_third)
        )
        every { taskRepositoryMock.getTasksByProjectNameDesc() } returns flowOf(
            listOf(task4_3_third, task1_2_second, task2_2_fourth, task3_1_first)
        )
        every { taskRepositoryMock.getTasksByDateAsc() } returns flowOf(
            listOf(task3_1_first, task1_2_second, task4_3_third, task2_2_fourth)
        )
        every { taskRepositoryMock.getTasksByDateDesc() } returns flowOf(
            listOf(task2_2_fourth, task4_3_third, task1_2_second, task3_1_first)
        )
        every { taskRepositoryMock.getAllTasks() } returns flowOf(
            listOf(task3_1_first, task4_3_third, task1_2_second, task2_2_fourth)
        )

        every { projectRepositoryMock.getAllProjects() } returns flowOf(
            listOf(project1, project2, project3)
        )

        getAllTasksWithProjectsUseCase = GetAllTasksWithProjectsUseCaseImpl(
            taskRepository = taskRepositoryMock,
            projectRepository = projectRepositoryMock,
        )
    }

    @Test
    fun `return the task list sorted by ascending name`() = runTest {
        // WHEN
        getAllTasksWithProjectsUseCase(TaskSortingType.TASK_NAME_ASC).collect {

            // THEN
            assertEquals(
                mapOf(
                    task1_2_second to project2,
                    task2_2_fourth to project2,
                    task3_1_first to project1,
                    task4_3_third to project3,
                ).toList(),
                it.toList()
            )
        }
    }

    @Test
    fun `return the task list sorted by descending name`() = runTest {
        // WHEN
        getAllTasksWithProjectsUseCase(TaskSortingType.TASK_NAME_DESC).collect {

            // THEN
            assertEquals(
                mapOf(
                    task4_3_third to project3,
                    task3_1_first to project1,
                    task2_2_fourth to project2,
                    task1_2_second to project2,
                ).toList(),
                it.toList()
            )
        }
    }

    @Test
    fun `return the task list sorted by ascending project name`() = runTest {
        // WHEN
        getAllTasksWithProjectsUseCase(TaskSortingType.PROJECT_NAME_ASC).collect {

            // THEN
            assertEquals(
                mapOf(
                    task3_1_first to project1,
                    task1_2_second to project2,
                    task2_2_fourth to project2,
                    task4_3_third to project3,
                ).toList(),
                it.toList()
            )
        }
    }

    @Test
    fun `return the task list sorted by descending project name`() = runTest {
        // WHEN
        getAllTasksWithProjectsUseCase(TaskSortingType.PROJECT_NAME_DESC).collect {

            // THEN
            assertEquals(
                mapOf(
                    task4_3_third to project3,
                    task1_2_second to project2,
                    task2_2_fourth to project2,
                    task3_1_first to project1
                ).toList(),
                it.toList()
            )
        }
    }

    @Test
    fun `return the task list sorted by ascending date`() = runTest {
        // WHEN
        getAllTasksWithProjectsUseCase(TaskSortingType.DATE_ASC).collect {

            // THEN
            assertEquals(
                mapOf(
                    task3_1_first to project1,
                    task1_2_second to project2,
                    task4_3_third to project3,
                    task2_2_fourth to project2
                ).toList(),
                it.toList()
            )
        }
    }

    @Test
    fun `return the task list sorted by descending date`() = runTest {
        // WHEN
        getAllTasksWithProjectsUseCase(TaskSortingType.DATE_DESC).collect {

            // THEN
            assertEquals(
                mapOf(
                    task2_2_fourth to project2,
                    task4_3_third to project3,
                    task1_2_second to project2,
                    task3_1_first to project1
                ).toList(),
                it.toList()
            )
        }
    }

    @Test
    fun `return the task list without any sorting type`() = runTest {
        // WHEN
        getAllTasksWithProjectsUseCase(TaskSortingType.NONE).collect {

            // THEN
            assertEquals(
                mapOf(
                    task3_1_first to project1,
                    task4_3_third to project3,
                    task1_2_second to project2,
                    task2_2_fourth to project2
                ).toList(),
                it.toList()
            )
        }
    }
}