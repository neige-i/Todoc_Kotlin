package fr.neige_i.todoc_kotlin.domain

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

@ExperimentalCoroutinesApi
class DeleteTaskUseCaseImplTest {

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    private lateinit var deleteTaskUseCase: DeleteTaskUseCase

    @MockK
    private lateinit var taskRepositoryMock: TaskRepository

    @Before
    fun setUp() {
        MockKAnnotations.init((this))

        coEvery { taskRepositoryMock.deleteTask(any()) } returns Unit

        deleteTaskUseCase = DeleteTaskUseCaseImpl(
            taskRepository = taskRepositoryMock,
            ioDispatcher = testCoroutineRule.testCoroutineDispatcher
        )
    }

    @Test
    fun `verify the repository deletes the task with the specified id`() = runTest {
        // WHEN
        deleteTaskUseCase(42)

        // THEN
        coVerify(exactly = 1) { taskRepositoryMock.deleteTask(42) }
        confirmVerified(taskRepositoryMock)
    }
}