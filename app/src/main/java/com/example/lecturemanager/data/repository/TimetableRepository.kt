package com.example.lecturemanager.data.repository

import com.example.lecturemanager.data.local.dao.TimetableDao
import com.example.lecturemanager.data.local.entity.TimetableEntity
import com.example.lecturemanager.data.remote.api.LecManApi
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TimetableRepository @Inject constructor(
    private val api: LecManApi,
    private val dao: TimetableDao
) {
    fun getTimetableForDay(day: Int): Flow<List<TimetableEntity>> = dao.getTimetableForDay(day)

    suspend fun refreshTimetable(token: String) {
        try {
            val remoteTimetable = api.getTimetable("Bearer $token")
            dao.clearTimetable()
            dao.insertTimetable(remoteTimetable)
        } catch (e: Exception) {
            // Handle error (e.g., log it)
        }
    }
}