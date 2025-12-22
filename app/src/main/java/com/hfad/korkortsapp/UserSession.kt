package com.hfad.korkortsapp

import android.content.Context
import java.util.UUID

object UserSession {
    private const val PREFS = "user_session"
    private const val KEY_USER_ID = "user_id"
    private const val KEY_USERNAME = "username"

    fun getOrCreateUserId(context: Context): String {
        val prefs = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
        val existing = prefs.getString(KEY_USER_ID, null)
        if (!existing.isNullOrBlank()) return existing

        val newId = UUID.randomUUID().toString()
        prefs.edit().putString(KEY_USER_ID, newId).apply()
        return newId
    }

    fun setUsername(context: Context, username: String) {
        context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
            .edit()
            .putString(KEY_USERNAME, username.trim())
            .apply()
    }

    fun getUsername(context: Context): String? {
        return context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
            .getString(KEY_USERNAME, null)
            ?.trim()
            ?.takeIf { it.isNotBlank() }
    }

    fun clear(context: Context) {
        context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
            .edit()
            .clear()
            .apply()
    }
}
