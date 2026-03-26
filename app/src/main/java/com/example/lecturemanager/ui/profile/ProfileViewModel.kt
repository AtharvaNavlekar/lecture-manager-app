package com.example.lecturemanager.ui.profile

import androidx.lifecycle.ViewModel
import com.example.lecturemanager.data.local.prefs.SessionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _profileState = MutableStateFlow(ProfileUiState())
    val profileState: StateFlow<ProfileUiState> = _profileState.asStateFlow()

    init {
        loadProfile()
    }

    private fun loadProfile() {
        _profileState.value = ProfileUiState(
            name = sessionManager.fetchUserName() ?: "Unknown",
            userId = sessionManager.fetchUserId() ?: "",
            role = sessionManager.fetchUserRole() ?: "faculty"
        )
    }

    fun logout() {
        sessionManager.clearSession()
    }
}
