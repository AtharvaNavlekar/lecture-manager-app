package com.example.lecturemanager.data.local.prefs

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SessionManager @Inject constructor(@ApplicationContext context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("lecman_prefs", Context.MODE_PRIVATE)

    companion object {
        private const val USER_TOKEN = "user_token"
        private const val USER_ROLE = "user_role"
        private const val USER_ID = "user_id"
    }

    fun saveAuthToken(token: String) {
        prefs.edit { putString(USER_TOKEN, token) }
    }

    fun fetchAuthToken(): String? {
        return prefs.getString(USER_TOKEN, null)
    }

    fun saveUserRole(role: String) {
        prefs.edit { putString(USER_ROLE, role) }
    }

    fun fetchUserRole(): String? {
        return prefs.getString(USER_ROLE, null)
    }

    fun saveUserId(id: String) {
        prefs.edit { putString(USER_ID, id) }
    }

    fun fetchUserId(): String? {
        return prefs.getString(USER_ID, null)
    }

    fun clearSession() {
        prefs.edit { clear() }
    }
}