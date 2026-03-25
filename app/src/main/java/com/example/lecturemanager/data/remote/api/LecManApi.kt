package com.example.lecturemanager.data.remote.api

import com.example.lecturemanager.data.local.entity.TimetableEntity
import retrofit2.http.GET
import retrofit2.http.Header

interface LecManApi {
    @GET("timetable")
    suspend fun getTimetable(
        @Header("Authorization") token: String
    ): List<TimetableEntity>
    
    // Add more endpoints for Leave requests, Substitute management, etc.
}