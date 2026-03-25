package com.example.lecturemanager.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "timetable")
data class TimetableEntity(
    @PrimaryKey val id: String,
    val subject: String,
    val teacherName: String,
    val startTime: String,
    val endTime: String,
    val roomNumber: String,
    val dayOfWeek: Int // 1 for Monday, etc.
)