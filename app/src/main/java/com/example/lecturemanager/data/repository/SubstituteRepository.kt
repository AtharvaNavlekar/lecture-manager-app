package com.example.lecturemanager.data.repository

import com.example.lecturemanager.data.remote.api.LecManApi
import com.example.lecturemanager.data.remote.dto.SubstituteRequest
import com.example.lecturemanager.util.Resource
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Repository that encapsulates all substitute-request related
 * network calls, keeping the ViewModel free of Retrofit details.
 */
@Singleton
class SubstituteRepository @Inject constructor(
    private val api: LecManApi
) {

    suspend fun getPendingRequests(): Resource<List<SubstituteRequest>> {
        return try {
            val response = api.getPendingSubstituteRequests()
            if (response.isSuccessful) {
                Resource.Success(response.body() ?: emptyList())
            } else {
                Resource.Error("Failed: ${response.code()}")
            }
        } catch (e: Exception) {
            Resource.Error(e.localizedMessage ?: "Network error")
        }
    }

    suspend fun acceptRequest(id: String): Resource<String> {
        return try {
            val response = api.acceptSubstituteRequest(id)
            if (response.isSuccessful) {
                Resource.Success("Accepted successfully")
            } else {
                Resource.Error("Failed: ${response.code()}")
            }
        } catch (e: Exception) {
            Resource.Error(e.localizedMessage ?: "Network error")
        }
    }

    suspend fun declineRequest(id: String): Resource<String> {
        return try {
            val response = api.declineSubstituteRequest(id)
            if (response.isSuccessful) {
                Resource.Success("Declined")
            } else {
                Resource.Error("Failed: ${response.code()}")
            }
        } catch (e: Exception) {
            Resource.Error(e.localizedMessage ?: "Network error")
        }
    }
}
