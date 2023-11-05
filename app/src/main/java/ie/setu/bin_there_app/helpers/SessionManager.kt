package ie.setu.bin_there_app.helpers

import android.content.Context
import android.content.SharedPreferences

class SessionManager(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    var loggedInUserId: Long
        get() = prefs.getLong(USER_ID, -1)
        set(value) = prefs.edit().putLong(USER_ID, value).apply()

    var isLoggedIn: Boolean
        get() = prefs.getBoolean(IS_LOGGED_IN, false)
        set(value) = prefs.edit().putBoolean(IS_LOGGED_IN, value).apply()

    // TODO
    fun logout() {
        prefs.edit().clear().apply()
    }

    companion object {
        private const val PREF_NAME = "UserSession"
        private const val USER_ID = "logged_in_user_id"
        private const val IS_LOGGED_IN = "is_logged_in"
    }
}
