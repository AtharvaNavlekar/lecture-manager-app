package com.example.lecturemanager.data.repository

import com.example.lecturemanager.data.local.dao.TimetableDao
import com.example.lecturemanager.data.local.entity.TimetableEntity
import com.example.lecturemanager.data.remote.api.LecManApi
import com.example.lecturemanager.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Single Source of Truth repository pattern:
 * 1. Emit Loading state
 * 2. Emit cached data from Room (if any)
 * 3. Fetch fresh data from Express API
 * 4. Save to Room
 * 5. Emit Success with fresh data from Room
 */
@Singleton
class TimetableRepository @Inject constructor(
    private val api: LecManApi,
    private val dao: TimetableDao
) {
    /**
     * Observes timetable for a given day from the local Room DB.
     */
    fun getTimetableForDay(day: Int): Flow<List<TimetableEntity>> = dao.getTimetableForDay(day)

    /**
     * Single Source of Truth flow: emits Loading → cached → network refresh → Success/Error.
     */
    fun getTimetableResource(day: Int): Flow<Resource<List<TimetableEntity>>> = flow {
        emit(Resource.Loading())

        // 1. Emit cached data immediately
        dao.getTimetableForDay(day).collect { cached ->
            emit(Resource.Loading(cached))
        }
    }

    /**
     * Fetches fresh timetable from the Express API, clears old cache, and inserts new data.
     * The AuthInterceptor automatically attaches the JWT token.
     */
    suspend fun refreshTimetable(): Resource<Unit> {
        return try {
            val response = api.getTimetable()
            if (response.isSuccessful) {
                response.body()?.let { remoteTimetable ->
                    dao.clearTimetable()
                    dao.insertTimetable(remoteTimetable)
                }
                Resource.Success(Unit)
            } else {
                Resource.Error("Server error: ${response.code()}")
            }
        } catch (e: Exception) {
            Resource.Error(e.localizedMessage ?: "Network error")
        }
    }
}