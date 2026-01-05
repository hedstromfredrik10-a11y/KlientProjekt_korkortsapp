package com.hfad.korkortsapp

import android.content.Context

object UserSession {
    private const val PREFS = "user_session"
    private const val KEY_USERNAME = "username"

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

    fun isLoggedIn(context: Context): Boolean =
        getUsername(context) != null

    fun clear(context: Context) {
        context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
            .edit()
            .clear()
            .apply()
    }
}
