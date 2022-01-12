package fr.neige_i.todoc_kotlin.domain

import fr.neige_i.todoc_kotlin.data.model.Task
import fr.neige_i.todoc_kotlin.data.repository.TaskRepository
import fr.neige_i.todoc_kotlin.util.TestCoroutineRule
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.time.Clock
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset

@ExperimentalCoroutinesApi
class AddNewTaskUseCaseImplTest {

    companion object {
        // IN
        private const val TASK_NAME = "task name"
        private const val PROJECT_ID = 42L

        // OUT
        private val TASK_TO_ADD = Task(
            projectId = PROJECT_ID,
            name = TASK_NAME,
            creationTimestamp = 1641830835000, // 2022/01/10 17:07:15 (GMT+1)
        )
    }

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    private lateinit var addNewTaskUseCase: AddNewTaskUseCase

    @MockK
    private lateinit var taskRepositoryMock: TaskRepository
    private val fixedClock = Clock.fixed(
        LocalDateTime.of(2022, 1, 10, 17, 7, 15).toInstant(ZoneOffset.of("+0100")),
        ZoneId.of("Z")
    )

    @Before
    fun setUp() {
        MockKAnnotations.init((this))

        coEvery { taskRepositoryMock.addTask(any()) } returns Unit

        addNewTaskUseCase = AddNewTaskUseCaseImpl(
            taskRepositoryMock,
            testCoroutineRule.testCoroutineDispatcher,
            fixedClock
        )
    }

    @Test
    fun `verify the repository adds the specified task`() = runTest {
        // WHEN
        addNewTaskUseCase(PROJECT_ID, TASK_NAME)

        // THEN
        coVerify(exactly = 1) { taskRepositoryMock.addTask(TASK_TO_ADD) }
        confirmVerified(taskRepositoryMock)
    }
}