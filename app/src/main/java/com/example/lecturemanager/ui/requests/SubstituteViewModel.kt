package com.example.lecturemanager.ui.requests

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lecturemanager.data.remote.dto.SubstituteRequest
import com.example.lecturemanager.data.repository.SubstituteRepository
import com.example.lecturemanager.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for the Substitute Action Screen.
 * Manages the 15-minute countdown timer and accept/decline actions.
 */
@HiltViewModel
class SubstituteViewModel @Inject constructor(
    private val repository: SubstituteRepository
) : ViewModel() {

    // --- Pending Substitute Requests ---
    private val _substituteRequests = MutableStateFlow<Resource<List<SubstituteRequest>>>(Resource.Loading())
    val substituteRequests: StateFlow<Resource<List<SubstituteRequest>>> = _substituteRequests.asStateFlow()

    // --- Active Request (the one being viewed) ---
    private val _activeRequest = MutableStateFlow<SubstituteRequest?>(null)
    val activeRequest: StateFlow<SubstituteRequest?> = _activeRequest.asStateFlow()

    // --- 15-minute Countdown Timer ---
    private val _remainingTimeMs = MutableStateFlow(0L)
    val remainingTimeMs: StateFlow<Long> = _remainingTimeMs.asStateFlow()

    // --- Action Result ---
    private val _actionResult = MutableStateFlow<Resource<String>?>(null)
    val actionResult: StateFlow<Resource<String>?> = _actionResult.asStateFlow()

    private var countdownJob: Job? = null

    init {
        fetchPendingRequests()
    }

    fun fetchPendingRequests() {
        viewModelScope.launch {
            _substituteRequests.value = Resource.Loading()
            _substituteRequests.value = repository.getPendingRequests()
        }
    }

    /**
     * Sets the active substitute request and starts the 15-minute countdown.
     * The countdown is driven by the `expiresAt` timestamp from the backend.
     */
    fun setActiveRequest(request: SubstituteRequest) {
        _activeRequest.value = request
        startCountdown(request.expiresAt)
    }

    /**
     * Coroutine-based countdown timer.
     * Ticks every second, exposing remaining time via StateFlow.
     * When time runs out, it automatically clears the active request.
     */
    private fun startCountdown(expiresAtMs: Long) {
        countdownJob?.cancel()
        countdownJob = viewModelScope.launch {
            while (true) {
                val remaining = expiresAtMs - System.currentTimeMillis()
                if (remaining <= 0) {
                    _remainingTimeMs.value = 0
                    _actionResult.value = Resource.Error("Request expired — auto-assigned by system")
                    break
                }
                _remainingTimeMs.value = remaining
                delay(1000L)
            }
        }
    }

    fun acceptRequest() {
        val request = _activeRequest.value ?: return
        viewModelScope.launch {
            _actionResult.value = Resource.Loading()
            val result = repository.acceptRequest(request.id)
            if (result is Resource.Success) countdownJob?.cancel()
            _actionResult.value = result
        }
    }

    fun declineRequest() {
        val request = _activeRequest.value ?: return
        viewModelScope.launch {
            _actionResult.value = Resource.Loading()
            val result = repository.declineRequest(request.id)
            if (result is Resource.Success) countdownJob?.cancel()
            _actionResult.value = result
        }
    }

    /**
     * Formats remaining milliseconds into a MM:SS string for the UI.
     */
    fun formatTime(ms: Long): String {
        val totalSeconds = ms / 1000
        val minutes = totalSeconds / 60
        val seconds = totalSeconds % 60
        return "%02d:%02d".format(minutes, seconds)
    }

    override fun onCleared() {
        super.onCleared()
        countdownJob?.cancel()
    }
}
