package com.example.lecturemanager.ui.requests

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lecturemanager.data.local.entity.LeaveRequestEntity
import com.example.lecturemanager.data.local.dao.LeaveDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RequestsViewModel @Inject constructor(
    private val leaveDao: LeaveDao
) : ViewModel() {

    private val _requestsState = MutableStateFlow<List<LeaveRequestEntity>>(emptyList())
    val requestsState: StateFlow<List<LeaveRequestEntity>> = _requestsState.asStateFlow()

    fun loadMyRequests(userId: String) {
        viewModelScope.launch {
            leaveDao.getMyLeaveRequests(userId).collect {
                _requestsState.value = it
            }
        }
    }

    fun loadHODPendingRequests() {
        viewModelScope.launch {
            leaveDao.getPendingRequestsForHOD().collect {
                _requestsState.value = it
            }
        }
    }

    fun updateRequestStatus(request: LeaveRequestEntity, newStatus: String) {
        viewModelScope.launch {
            val updatedRequest = request.copy(status = newStatus)
            leaveDao.updateLeaveStatus(updatedRequest)
            // In a real app, also call the API here
        }
    }
}