package com.example.lecturemanager.data.local.dao

import androidx.room.*
import com.example.lecturemanager.data.local.entity.TimetableEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TimetableDao {
    @Query("SELECT * FROM timetable WHERE dayOfWeek = :day")
    fun getTimetableForDay(day: Int): Flow<List<TimetableEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTimetable(timetable: List<TimetableEntity>)

    @Query("DELETE FROM timetable")
    suspend fun clearTimetable()
}