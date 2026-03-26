package com.example.lecturemanager.ui.profile

import com.example.lecturemanager.data.local.prefs.SessionManager
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

class ProfileViewModelTest {

    private lateinit var sessionManager: SessionManager
    private lateinit var viewModel: ProfileViewModel

    @Before
    fun setUp() {
        sessionManager = mock()
    }

    @Test
    fun `loadProfile populates state from SessionManager`() {
        whenever(sessionManager.fetchUserName()).thenReturn("Dr. Smith")
        whenever(sessionManager.fetchUserId()).thenReturn("user123")
        whenever(sessionManager.fetchUserRole()).thenReturn("hod")

        viewModel = ProfileViewModel(sessionManager)

        val state = viewModel.profileState.value
        assertEquals("Dr. Smith", state.name)
        assertEquals("user123", state.userId)
        assertEquals("hod", state.role)
    }

    @Test
    fun `loadProfile uses defaults when SessionManager returns null`() {
        whenever(sessionManager.fetchUserName()).thenReturn(null)
        whenever(sessionManager.fetchUserId()).thenReturn(null)
        whenever(sessionManager.fetchUserRole()).thenReturn(null)

        viewModel = ProfileViewModel(sessionManager)

        val state = viewModel.profileState.value
        assertEquals("Unknown", state.name)
        assertEquals("", state.userId)
        assertEquals("faculty", state.role)
    }

    @Test
    fun `logout clears session via SessionManager`() {
        whenever(sessionManager.fetchUserName()).thenReturn("Test")
        whenever(sessionManager.fetchUserId()).thenReturn("id")
        whenever(sessionManager.fetchUserRole()).thenReturn("admin")

        viewModel = ProfileViewModel(sessionManager)
        viewModel.logout()

        verify(sessionManager).clearSession()
    }
}
