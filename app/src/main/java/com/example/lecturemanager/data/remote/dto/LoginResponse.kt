package com.example.lecturemanager.data.remote.dto

data class LoginResponse(
    val token: String,
    val userId: String,
    val role: String, // "faculty", "hod", "admin"
    val name: String
)
