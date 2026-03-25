package com.example.lecturemanager.data.local.dao

import androidx.room.*
import com.example.lecturemanager.data.local.entity.LeaveRequestEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface LeaveDao {
    @Query("SELECT * FROM leave_requests WHERE applicantId = :userId")
    fun getMyLeaveRequests(userId: String): Flow<List<LeaveRequestEntity>>

    @Query("SELECT * FROM leave_requests WHERE status = 'PENDING'")
    fun getPendingRequestsForHOD(): Flow<List<LeaveRequestEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLeaveRequest(request: LeaveRequestEntity)

    @Update
    suspend fun updateLeaveStatus(request: LeaveRequestEntity)
}