package id.nns.nico_chat.utils

import android.content.Context
import id.nns.nico_chat.data.response.google_sheet.UserResponse

class UserPreference(context: Context) {
    companion object {
        private const val LOGIN_PREF = "login_pref"
        private const val ID = "id"
        private const val NAME = "name"
        private const val EMAIL = "email"
        private const val PHOTO_URL = "photo_url"
    }

    private val pref = context.getSharedPreferences(LOGIN_PREF, Context.MODE_PRIVATE)

    fun setUserPref(userResponse: UserResponse) {
        val editor = pref.edit()
        editor.putString(ID, userResponse.id)
        editor.putString(NAME, userResponse.name)
        editor.putString(EMAIL, userResponse.email)
        editor.putString(PHOTO_URL, userResponse.photoUrl)
        editor.apply()
    }

    fun getUserPref() : UserResponse = UserResponse(
        pref.getString(ID, "").toString(),
        pref.getString(NAME, "").toString(),
        pref.getString(EMAIL, "").toString(),
        pref.getString(PHOTO_URL, "").toString(),
        true
    )

    fun clearLoginPref() {
        pref.edit().clear().apply()
    }
}
