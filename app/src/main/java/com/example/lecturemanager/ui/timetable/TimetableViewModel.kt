package com.example.lecturemanager.ui.timetable

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lecturemanager.data.local.entity.TimetableEntity
import com.example.lecturemanager.data.repository.TimetableRepository
import com.example.lecturemanager.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject

/**
 * ViewModel implementing the "Single Source of Truth" pattern:
 * 1. UI observes local Room data via StateFlow.
 * 2. On init or pull-to-refresh, ViewModel triggers a network fetch.
 * 3. Network data is saved to Room.
 * 4. Room emits the update automatically through the Flow, keeping UI in sync.
 */
@HiltViewModel
class TimetableViewModel @Inject constructor(
    private val repository: TimetableRepository
) : ViewModel() {

    private val _timetableState = MutableStateFlow<Resource<List<TimetableEntity>>>(Resource.Loading())
    val timetableState: StateFlow<Resource<List<TimetableEntity>>> = _timetableState.asStateFlow()

    private val _selectedDay = MutableStateFlow(getCurrentDayOfWeek())
    val selectedDay: StateFlow<Int> = _selectedDay.asStateFlow()

    init {
        observeTimetable()
        refreshFromNetwork()
    }

    /**
     * Observes the local Room database for the selected day.
     * Any changes to Room (from network refresh) will automatically propagate here.
     */
    private fun observeTimetable() {
        viewModelScope.launch {
            _selectedDay.collect { day ->
                repository.getTimetableForDay(day).collect { data ->
                    _timetableState.value = if (data.isEmpty()) {
                        Resource.Loading(data) // Still loading if empty
                    } else {
                        Resource.Success(data)
                    }
                }
            }
        }
    }

    /**
     * Fetches fresh data from the Express backend and saves it to Room.
     * Room's Flow will automatically emit the update to the UI.
     */
    fun refreshFromNetwork() {
        viewModelScope.launch {
            _timetableState.value = Resource.Loading(_timetableState.value.data)
            val result = repository.refreshTimetable()
            if (result is Resource.Error) {
                _timetableState.value = Resource.Error(
                    result.message ?: "Failed to refresh",
                    _timetableState.value.data
                )
            }
            // On success, Room's Flow (observed in observeTimetable) will emit the updated data.
        }
    }

    fun selectDay(day: Int) {
        _selectedDay.value = day
    }

    private fun getCurrentDayOfWeek(): Int {
        return Calendar.getInstance().get(Calendar.DAY_OF_WEEK)
    }
}
