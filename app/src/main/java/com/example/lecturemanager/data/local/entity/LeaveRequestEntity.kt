package com.example.lecturemanager.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "leave_requests")
data class LeaveRequestEntity(
    @PrimaryKey val requestId: String,
    val applicantName: String,
    val applicantId: String,
    val startDate: String,
    val endDate: String,
    val reason: String,
    val status: String, // PENDING, APPROVED, REJECTED
    val substituteTeacherId: String?,
    val substituteStatus: String? // PENDING, ACCEPTED, DECLINED
)