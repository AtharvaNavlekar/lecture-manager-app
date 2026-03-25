package com.example.lecturemanager.ui.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lecturemanager.data.local.entity.TimetableEntity
import com.example.lecturemanager.data.repository.TimetableRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val repository: TimetableRepository
) : ViewModel() {

    private val _timetableState = MutableStateFlow<List<TimetableEntity>>(emptyList())
    val timetableState: StateFlow<List<TimetableEntity>> = _timetableState.asStateFlow()

    init {
        loadTodayTimetable()
    }

    private fun loadTodayTimetable() {
        val calendar = Calendar.getInstance()
        val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) // 1=Sun, 2=Mon...
        
        viewModelScope.launch {
            repository.getTimetableForDay(dayOfWeek).collect {
                _timetableState.value = it
            }
        }
    }

    fun refreshData(token: String) {
        viewModelScope.launch {
            repository.refreshTimetable(token)
        }
    }
}