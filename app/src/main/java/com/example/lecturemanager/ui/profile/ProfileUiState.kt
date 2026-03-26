package com.example.lecturemanager.ui.profile

data class ProfileUiState(
    val name: String = "",
    val userId: String = "",
    val role: String = ""   // "faculty", "hod", "admin"
)
