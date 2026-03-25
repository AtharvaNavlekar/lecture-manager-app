package com.example.lecturemanager.data.remote.dto

data class SubstituteRequest(
    val id: String,
    val requesterName: String,
    val courseName: String,
    val timeSlot: String,
    val room: String,
    val date: String,
    val status: String, // "pending", "accepted", "declined", "auto_assigned"
    val expiresAt: Long  // Unix timestamp (ms) for the 15-minute deadline
)
