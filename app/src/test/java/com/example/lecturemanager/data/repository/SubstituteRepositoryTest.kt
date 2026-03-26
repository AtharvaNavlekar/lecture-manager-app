package com.example.lecturemanager.data.repository

import com.example.lecturemanager.data.remote.api.LecManApi
import com.example.lecturemanager.data.remote.dto.SubstituteRequest
import com.example.lecturemanager.util.Resource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import retrofit2.Response
import java.io.IOException

@OptIn(ExperimentalCoroutinesApi::class)
class SubstituteRepositoryTest {

    private lateinit var api: LecManApi
    private lateinit var repository: SubstituteRepository

    private val sampleRequests = listOf(
        SubstituteRequest("1", "Dr. A", "Math", "09:00-10:00", "R101", "2025-02-01", "pending", 1700000000000L),
        SubstituteRequest("2", "Dr. B", "CS", "11:00-12:00", "R202", "2025-02-01", "pending", 1700000000000L)
    )

    @Before
    fun setUp() {
        api = mock()
        repository = SubstituteRepository(api)
    }

    // ---- getPendingRequests ----

    @Test
    fun `getPendingRequests returns Success on 200`() = runTest {
        whenever(api.getPendingSubstituteRequests()).thenReturn(Response.success(sampleRequests))

        val result = repository.getPendingRequests()

        assertTrue(result is Resource.Success)
        assertEquals(2, result.data!!.size)
    }

    @Test
    fun `getPendingRequests returns Success with empty list when body is null`() = runTest {
        whenever(api.getPendingSubstituteRequests()).thenReturn(Response.success(null))

        val result = repository.getPendingRequests()

        assertTrue(result is Resource.Success)
        assertTrue(result.data!!.isEmpty())
    }

    @Test
    fun `getPendingRequests returns Error on HTTP failure`() = runTest {
        whenever(api.getPendingSubstituteRequests()).thenReturn(
            Response.error(500, okhttp3.ResponseBody.create(null, ""))
        )

        val result = repository.getPendingRequests()

        assertTrue(result is Resource.Error)
        assertTrue(result.message!!.contains("500"))
    }

    @Test
    fun `getPendingRequests returns Error on network exception`() = runTest {
        whenever(api.getPendingSubstituteRequests()).thenAnswer { throw IOException("No internet") }

        val result = repository.getPendingRequests()

        assertTrue(result is Resource.Error)
        assertEquals("No internet", result.message)
    }

    // ---- acceptRequest ----

    @Test
    fun `acceptRequest returns Success on 200`() = runTest {
        whenever(api.acceptSubstituteRequest("1")).thenReturn(Response.success(Unit))

        val result = repository.acceptRequest("1")

        assertTrue(result is Resource.Success)
        assertEquals("Accepted successfully", result.data)
    }

    @Test
    fun `acceptRequest returns Error on HTTP failure`() = runTest {
        whenever(api.acceptSubstituteRequest("1")).thenReturn(
            Response.error(400, okhttp3.ResponseBody.create(null, ""))
        )

        val result = repository.acceptRequest("1")

        assertTrue(result is Resource.Error)
        assertTrue(result.message!!.contains("400"))
    }

    @Test
    fun `acceptRequest returns Error on exception`() = runTest {
        whenever(api.acceptSubstituteRequest("1")).thenAnswer { throw IOException("Timeout") }

        val result = repository.acceptRequest("1")

        assertTrue(result is Resource.Error)
        assertEquals("Timeout", result.message)
    }

    // ---- declineRequest ----

    @Test
    fun `declineRequest returns Success on 200`() = runTest {
        whenever(api.declineSubstituteRequest("2")).thenReturn(Response.success(Unit))

        val result = repository.declineRequest("2")

        assertTrue(result is Resource.Success)
        assertEquals("Declined", result.data)
    }

    @Test
    fun `declineRequest returns Error on HTTP failure`() = runTest {
        whenever(api.declineSubstituteRequest("2")).thenReturn(
            Response.error(403, okhttp3.ResponseBody.create(null, ""))
        )

        val result = repository.declineRequest("2")

        assertTrue(result is Resource.Error)
        assertTrue(result.message!!.contains("403"))
    }

    @Test
    fun `declineRequest returns Error on exception`() = runTest {
        whenever(api.declineSubstituteRequest("2")).thenAnswer { throw IOException("Connection reset") }

        val result = repository.declineRequest("2")

        assertTrue(result is Resource.Error)
        assertEquals("Connection reset", result.message)
    }
}
