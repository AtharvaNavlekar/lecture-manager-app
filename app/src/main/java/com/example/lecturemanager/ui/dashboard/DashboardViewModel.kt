package com.example.lecturemanager.ui.dashboard

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

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val repository: TimetableRepository
) : ViewModel() {

    private val _timetableState = MutableStateFlow<Resource<List<TimetableEntity>>>(Resource.Loading())
    val timetableState: StateFlow<Resource<List<TimetableEntity>>> = _timetableState.asStateFlow()

    init {
        loadTodayTimetable()
        refreshData()
    }

    private fun loadTodayTimetable() {
        val calendar = Calendar.getInstance()
        val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)

        viewModelScope.launch {
            repository.getTimetableForDay(dayOfWeek).collect { data ->
                _timetableState.value = if (data.isEmpty()) {
                    Resource.Loading(data)
                } else {
                    Resource.Success(data)
                }
            }
        }
    }

    fun refreshData() {
        viewModelScope.launch {
            _timetableState.value = Resource.Loading(_timetableState.value.data)
            val result = repository.refreshTimetable()
            if (result is Resource.Error) {
                _timetableState.value = Resource.Error(
                    result.message ?: "Failed to refresh",
                    _timetableState.value.data
                )
            }
        }
    }
}