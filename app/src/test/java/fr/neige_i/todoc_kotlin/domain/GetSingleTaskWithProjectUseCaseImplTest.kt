package fr.neige_i.todoc_kotlin.domain

import fr.neige_i.todoc_kotlin.data.model.Project
import fr.neige_i.todoc_kotlin.data.model.Task
import fr.neige_i.todoc_kotlin.data.repository.ProjectRepository
import fr.neige_i.todoc_kotlin.data.repository.TaskRepository
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
class GetSingleTaskWithProjectUseCaseImplTest {

    companion object {
        // IN
        private const val TASK_ID = 1L

        // OUT
        private const val PROJECT_ID = 2L
        private val TASK = Task(TASK_ID, PROJECT_ID, "name", 123)
    }

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    private lateinit var getSingleTaskWithProjectUseCase: GetSingleTaskWithProjectUseCase

    @MockK
    private lateinit var taskRepositoryMock: TaskRepository

    @MockK
    private lateinit var projectRepositoryMock: ProjectRepository

    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        every { taskRepositoryMock.getTask(TASK_ID) } returns flowOf(TASK)

        every { projectRepositoryMock.getAllProjects() } returns flowOf(listOf(
            getDefaultProject(1),
            getDefaultProject(2),
            getDefaultProject(3),
        ))

        getSingleTaskWithProjectUseCase = GetSingleTaskWithProjectUseCaseImpl(
            taskRepositoryMock,
            projectRepositoryMock,
        )
    }

    @Test
    fun `return the pair of both Task and Project from Flows`() = runTest {
        // WHEN
        getSingleTaskWithProjectUseCase(TASK_ID).collect {

            // THEN
            assertEquals(Pair(TASK, getDefaultProject(PROJECT_ID)), it)
        }
    }

    // region OUT

    private fun getDefaultProject(index: Long) = Project(
        index,
        "name_$index",
        index.toInt()
    )

    // endregion OUT
}