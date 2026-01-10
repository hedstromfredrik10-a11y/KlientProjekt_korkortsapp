package com.hfad.korkortsapp

import android.content.Context

/**
 * Objekt som hanterar användarens inloggningssession lokalt.
 *
 * Informationen sparas i SharedPreferences och används för att
 * hålla reda på vilken användare som är inloggad.
 */
object UserSession {

    /**
     * Namn på SharedPreferences-filen.
     */
    private const val PREFS = "user_session"

    /**
     * Nyckel för lagring av användarnamn.
     */
    private const val KEY_USERNAME = "username"

    /**
     * Sparar användarnamnet i SharedPreferences.
     *
     * @param context Kontext som används för att komma åt SharedPreferences.
     * @param username Användarnamn som ska sparas.
     */
    fun setUsername(context: Context, username: String) {
        context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
            .edit()
            .putString(KEY_USERNAME, username.trim())
            .apply()
    }

    /**
     * Hämtar sparat användarnamn från SharedPreferences.
     *
     * @param context Kontext som används för att läsa SharedPreferences.
     * @return Användarnamnet om det finns, annars null.
     */
    fun getUsername(context: Context): String? {
        return context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
            .getString(KEY_USERNAME, null)
            ?.trim()
            ?.takeIf { it.isNotBlank() }
    }

    /**
     * Rensar all sparad sessionsdata.
     *
     * Används vid utloggning.
     *
     * @param context Kontext som används för att rensa SharedPreferences.
     */
    fun clear(context: Context) {
        context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
            .edit()
            .clear()
            .apply()
    }
}
