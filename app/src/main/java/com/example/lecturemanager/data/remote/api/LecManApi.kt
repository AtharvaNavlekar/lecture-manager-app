package com.example.lecturemanager.data.remote.api

import com.example.lecturemanager.data.local.entity.LeaveRequestEntity
import com.example.lecturemanager.data.local.entity.TimetableEntity
import com.example.lecturemanager.data.remote.dto.LoginRequest
import com.example.lecturemanager.data.remote.dto.LoginResponse
import com.example.lecturemanager.data.remote.dto.SubstituteRequest
import retrofit2.Response
import retrofit2.http.*

interface LecManApi {

    // --- Auth ---
    @POST("api/auth/login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>

    // --- Timetable ---
    @GET("api/timetable")
    suspend fun getTimetable(): Response<List<TimetableEntity>>

    // --- Leave Requests ---
    @GET("api/leaves")
    suspend fun getLeaveRequests(): Response<List<LeaveRequestEntity>>

    @POST("api/leaves")
    suspend fun createLeaveRequest(@Body request: LeaveRequestEntity): Response<LeaveRequestEntity>

    // --- Substitutes ---
    @GET("api/substitutes/pending")
    suspend fun getPendingSubstituteRequests(): Response<List<SubstituteRequest>>

    @POST("api/substitutes/{id}/accept")
    suspend fun acceptSubstituteRequest(@Path("id") id: String): Response<Unit>

    @POST("api/substitutes/{id}/decline")
    suspend fun declineSubstituteRequest(@Path("id") id: String): Response<Unit>
}