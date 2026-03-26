package com.example.lecturemanager.ui.timetable

import com.example.lecturemanager.data.local.entity.TimetableEntity
import com.example.lecturemanager.data.repository.TimetableRepository
import com.example.lecturemanager.util.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import org.mockito.kotlin.verify
import org.mockito.kotlin.any

@OptIn(ExperimentalCoroutinesApi::class)
class TimetableViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var repository: TimetableRepository
    private lateinit var viewModel: TimetableViewModel

    // Shared flow so we can emit values at the right time (simulating Room)
    private val roomFlow = MutableSharedFlow<List<TimetableEntity>>(replay = 1)

    private val sampleEntities = listOf(
        TimetableEntity("1", "Math", "Dr. Smith", "09:00", "10:00", "R101", 2),
        TimetableEntity("2", "Physics", "Dr. Doe", "10:00", "11:00", "R102", 2)
    )

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        repository = mock()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state is Loading`() = runTest(testDispatcher) {
        whenever(repository.getTimetableForDay(any())).thenReturn(roomFlow)
        whenever(repository.refreshTimetable()).thenReturn(Resource.Success(Unit))

        viewModel = TimetableViewModel(repository)

        // Before advancing, state should be Loading
        assertTrue(viewModel.timetableState.value is Resource.Loading)
    }

    @Test
    fun `selectDay updates selectedDay state`() = runTest(testDispatcher) {
        whenever(repository.getTimetableForDay(any())).thenReturn(roomFlow)
        whenever(repository.refreshTimetable()).thenReturn(Resource.Success(Unit))

        viewModel = TimetableViewModel(repository)
        advanceUntilIdle()

        viewModel.selectDay(3)
        assertEquals(3, viewModel.selectedDay.value)
    }

    @Test
    fun `timetableState emits Success when data available`() = runTest(testDispatcher) {
        whenever(repository.getTimetableForDay(any())).thenReturn(roomFlow)
        whenever(repository.refreshTimetable()).thenAnswer {
            // Simulate Room emitting data after network refresh saves to DB
            roomFlow.tryEmit(sampleEntities)
            Resource.Success(Unit)
        }

        viewModel = TimetableViewModel(repository)
        advanceUntilIdle()

        val state = viewModel.timetableState.value
        assertTrue("Expected Success but got ${state::class.simpleName}", state is Resource.Success)
        assertEquals(2, state.data!!.size)
    }

    @Test
    fun `refreshFromNetwork sets Error when repository fails`() = runTest(testDispatcher) {
        whenever(repository.getTimetableForDay(any())).thenReturn(roomFlow)
        whenever(repository.refreshTimetable()).thenReturn(Resource.Error("Server down"))

        viewModel = TimetableViewModel(repository)
        advanceUntilIdle()

        val state = viewModel.timetableState.value
        assertTrue("Expected Error but got ${state::class.simpleName}", state is Resource.Error)
        assertEquals("Server down", state.message)
    }

    @Test
    fun `refreshFromNetwork calls repository refreshTimetable`() = runTest(testDispatcher) {
        whenever(repository.getTimetableForDay(any())).thenReturn(roomFlow)
        whenever(repository.refreshTimetable()).thenReturn(Resource.Success(Unit))

        viewModel = TimetableViewModel(repository)
        advanceUntilIdle()

        // init already called refresh once; call again
        viewModel.refreshFromNetwork()
        advanceUntilIdle()

        verify(repository, org.mockito.kotlin.times(2)).refreshTimetable()
    }
}
