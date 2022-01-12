package fr.neige_i.todoc_kotlin.domain

import fr.neige_i.todoc_kotlin.data.model.Project
import fr.neige_i.todoc_kotlin.data.repository.ProjectRepository
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
class GetAllProjectsUseCaseImplTest {

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    private lateinit var getAllProjectsUseCase: GetAllProjectsUseCase

    @MockK
    private lateinit var projectRepositoryMock: ProjectRepository

    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        every { projectRepositoryMock.getAllProjects() } returns flowOf(getDefaultProjectList())

        getAllProjectsUseCase = GetAllProjectsUseCaseImpl(projectRepositoryMock)
    }

    @Test
    fun `return list of projects`() = runTest {
        // WHEN
        getAllProjectsUseCase().collect {

            // THEN
            assertEquals(getDefaultProjectList(), it)
        }
    }

    // region OUT

    private fun getDefaultProjectList() = listOf(
        Project(6L, "name_6", 0x6),
        Project(5L, "name_5", 0x5),
        Project(4L, "name_4", 0x4),
    )

    // endregion OUT
}