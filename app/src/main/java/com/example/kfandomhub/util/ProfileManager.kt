package com.example.kfandomhub.util

import android.content.Context
import android.content.SharedPreferences

class ProfileManager(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("user_profile", Context.MODE_PRIVATE)

    fun saveProfile(name: String, username: String, bio: String = getBio(), imageUri: String? = null) {
        prefs.edit().apply {
            putString("name", name)
            putString("username", username)
            putString("bio", bio)
            imageUri?.let { putString("profile_image", it) }
            apply()
        }
    }

    fun getName(): String = prefs.getString("name", "LuvlyBlink") ?: "LuvlyBlink"
    fun getUsername(): String = prefs.getString("username", "luvblink_13") ?: "luvblink_13"
    fun getBio(): String = prefs.getString("bio", "") ?: ""
    fun getProfileImage(): String? = prefs.getString("profile_image", null)
    
    fun isSet(): Boolean = prefs.contains("name")
}
