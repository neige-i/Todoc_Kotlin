package fr.neige_i.todoc_kotlin.ui.detail

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import fr.neige_i.todoc_kotlin.data.model.Project
import fr.neige_i.todoc_kotlin.data.model.Task
import fr.neige_i.todoc_kotlin.domain.GetSingleTaskWithProjectUseCase
import fr.neige_i.todoc_kotlin.ui.util.NavArgsProducer
import fr.neige_i.todoc_kotlin.util.TestCoroutineRule
import fr.neige_i.todoc_kotlin.util.TestLifecycleRule.getValueForTesting
import io.mockk.MockKAnnotations
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.time.ZoneId

class DetailViewModelTest {

    companion object {
        // IN
        private const val TASK_ID_ARG = 1L

        // OUT
        private const val TASK_ID_STRING = "1"
        private const val TASK_NAME = "TASK_1"
        private const val PROJECT_NAME = "PROJECT_2"
        private const val TIME_MILLIS = 1641830835386
        private const val TIME_STRING = "10/01/2022 17:07:15" // French local time
    }

    @ExperimentalCoroutinesApi
    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var detailViewModel: DetailViewModel

    @MockK
    private lateinit var navArgsProducerMock: NavArgsProducer

    @MockK
    private lateinit var getSingleTaskWithProjectUseCaseMock: GetSingleTaskWithProjectUseCase
    private val franceZoneId = ZoneId.of("GMT+1")

    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        every { navArgsProducerMock.getNavArgs(DetailBottomSheetArgs::class) } returns
                DetailBottomSheetArgs(TASK_ID_ARG)

        every { getSingleTaskWithProjectUseCaseMock(TASK_ID_ARG) } returns flowOf(
            Pair(
                Task(TASK_ID_ARG, 2, TASK_NAME, TIME_MILLIS),
                Project(2, PROJECT_NAME, 0x2)
            )
        )

        detailViewModel = DetailViewModel(
            navArgsProducer = navArgsProducerMock,
            getSingleTaskWithProjectUseCase = getSingleTaskWithProjectUseCaseMock,
            defaultZoneId = franceZoneId
        )

        verify(exactly = 1) { navArgsProducerMock.getNavArgs(DetailBottomSheetArgs::class) }
        verify(exactly = 1) { getSingleTaskWithProjectUseCaseMock.invoke(TASK_ID_ARG) }
    }

    @Test
    fun `test the nominal case`() {
        // WHEN
        // ViewModel is initialized

        val detailViewState = getValueForTesting(detailViewModel.viewState)

        // THEN
        assertEquals(
            DetailViewState(
                TASK_ID_STRING,
                TASK_NAME,
                PROJECT_NAME,
                TIME_STRING
            ),
            detailViewState
        )

        confirmVerified(navArgsProducerMock, getSingleTaskWithProjectUseCaseMock)
    }
}