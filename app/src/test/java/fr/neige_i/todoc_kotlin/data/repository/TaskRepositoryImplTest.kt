package fr.neige_i.todoc_kotlin.data.repository

import fr.neige_i.todoc_kotlin.data.data_source.TaskDao
import fr.neige_i.todoc_kotlin.data.model.Task
import fr.neige_i.todoc_kotlin.util.TestCoroutineRule
import io.mockk.*
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class TaskRepositoryImplTest {

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    private lateinit var taskRepository: TaskRepository

    @MockK
    private lateinit var taskDaoMock: TaskDao

    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        every { taskDaoMock.getTaskById(any()) } returns flowOf()
        every { taskDaoMock.getAllTasks() } returns flowOf()
        every { taskDaoMock.getTasksByNameAsc() } returns flowOf()
        every { taskDaoMock.getTasksByNameDesc() } returns flowOf()
        every { taskDaoMock.getTasksByProjectNameAsc() } returns flowOf()
        every { taskDaoMock.getTasksByProjectNameDesc() } returns flowOf()
        every { taskDaoMock.getTasksByDateAsc() } returns flowOf()
        every { taskDaoMock.getTasksByDateDesc() } returns flowOf()
        coJustRun { taskDaoMock.insert(any()) }
        coJustRun { taskDaoMock.deleteWithId(any()) }

        taskRepository = TaskRepositoryImpl(taskDaoMock)
    }

    @Test
    fun `verify the DAO calls getTaskById method`() = runTest {
        // WHEN
        taskRepository.getTask(42)

        // THEN
        coVerify(exactly = 1) { taskDaoMock.getTaskById(42) }
        confirmVerified(taskDaoMock)
    }

    @Test
    fun `verify the DAO calls getAllTasks method`() = runTest {
        // WHEN
        taskRepository.getAllTasks()

        // THEN
        coVerify(exactly = 1) { taskDaoMock.getAllTasks() }
        confirmVerified(taskDaoMock)
    }

    @Test
    fun `verify the DAO calls getTasksByNameAsc method`() = runTest {
        // WHEN
        taskRepository.getTasksByNameAsc()

        // THEN
        coVerify(exactly = 1) { taskDaoMock.getTasksByNameAsc() }
        confirmVerified(taskDaoMock)
    }

    @Test
    fun `verify the DAO calls getTasksByNameDesc method`() = runTest {
        // WHEN
        taskRepository.getTasksByNameDesc()

        // THEN
        coVerify(exactly = 1) { taskDaoMock.getTasksByNameDesc() }
        confirmVerified(taskDaoMock)
    }

    @Test
    fun `verify the DAO calls getTasksByProjectNameAsc method`() = runTest {
        // WHEN
        taskRepository.getTasksByProjectNameAsc()

        // THEN
        coVerify(exactly = 1) { taskDaoMock.getTasksByProjectNameAsc() }
        confirmVerified(taskDaoMock)
    }

    @Test
    fun `verify the DAO calls getTasksByProjectNameDesc method`() = runTest {
        // WHEN
        taskRepository.getTasksByProjectNameDesc()

        // THEN
        coVerify(exactly = 1) { taskDaoMock.getTasksByProjectNameDesc() }
        confirmVerified(taskDaoMock)
    }

    @Test
    fun `verify the DAO calls getTasksByDateAsc method`() = runTest {
        // WHEN
        taskRepository.getTasksByDateAsc()

        // THEN
        coVerify(exactly = 1) { taskDaoMock.getTasksByDateAsc() }
        confirmVerified(taskDaoMock)
    }

    @Test
    fun `verify the DAO calls getTasksByDateDesc method`() = runTest {
        // WHEN
        taskRepository.getTasksByDateDesc()

        // THEN
        coVerify(exactly = 1) { taskDaoMock.getTasksByDateDesc() }
        confirmVerified(taskDaoMock)
    }

    @Test
    fun `verify the DAO calls insert method`() = runTest {
        // GIVEN
        val taskToAdd = Task(42, 24, "task42", 4224)

        // WHEN
        taskRepository.addTask(taskToAdd)

        // THEN
        coVerify(exactly = 1) { taskDaoMock.insert(taskToAdd) }
        confirmVerified(taskDaoMock)
    }

    @Test
    fun `verify the DAO calls deleteWithId method`() = runTest {
        // WHEN
        taskRepository.deleteTask(42)

        // THEN
        coVerify(exactly = 1) { taskDaoMock.deleteWithId(42) }
        confirmVerified(taskDaoMock)
    }
}