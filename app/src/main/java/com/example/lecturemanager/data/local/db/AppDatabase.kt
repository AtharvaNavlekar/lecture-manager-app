package com.example.lecturemanager.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.lecturemanager.data.local.dao.TimetableDao
import com.example.lecturemanager.data.local.dao.LeaveDao
import com.example.lecturemanager.data.local.entity.TimetableEntity
import com.example.lecturemanager.data.local.entity.LeaveRequestEntity

@Database(entities = [TimetableEntity::class, LeaveRequestEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun timetableDao(): TimetableDao
    abstract fun leaveDao(): LeaveDao
}