package com.example.h_mal.stackexchangeusers.data.preferences

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager

private const val LAST_SAVED = "late_saved"
private const val USER_SAVED = "user_saved"
class PreferenceProvider(
    context: Context
) {

    private val appContext = context.applicationContext

    private val preference: SharedPreferences
        get() = PreferenceManager.getDefaultSharedPreferences(appContext)


    fun saveLastSavedAt(user: String, savedAt: Long) {
        preference.edit().putString(
            USER_SAVED,
            user
        ).putLong(
            LAST_SAVED,
            savedAt
        ).apply()
    }

    fun getLastSavedAt(user: String): Long? {
        val savedUser = preference.getString(USER_SAVED, null)

        if (savedUser == user){
            return preference.getLong(LAST_SAVED, 1595076034403)
        }
        return null
    }

}