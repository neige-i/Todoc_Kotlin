package fr.neige_i.todoc_kotlin.data.repository

import fr.neige_i.todoc_kotlin.data.data_source.ProjectDao
import fr.neige_i.todoc_kotlin.util.TestCoroutineRule
import io.mockk.MockKAnnotations
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class ProjectRepositoryImplTest {

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    private lateinit var projectRepository: ProjectRepository

    @MockK
    private lateinit var projectDaoMock: ProjectDao

    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        every { projectDaoMock.getAllProjects() } returns flowOf()

        projectRepository = ProjectRepositoryImpl(projectDaoMock)
    }

    @Test
    fun `verify the DAO calls getAllProjects method`() = runTest {
        // WHEN
        projectRepository.getAllProjects()

        // THEN
        coVerify(exactly = 1) { projectDaoMock.getAllProjects() }
        confirmVerified(projectDaoMock)
    }
}