package com.erwiin21mp.cinemovilplus.data.network

import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.analytics.logEvent
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.ktx.Firebase

class AnalyticsManager {
    private val analytics: FirebaseAnalytics by lazy { Firebase.analytics }

    private companion object {
        const val USER_ID = "UserId"
        const val USER_IS_ANONYMOUS = "UserIsAnonymous"
        const val USER_DISPLAY_NAME = "UserDisplayName"
        const val USER_EMAIL = "UserEmail"
        const val USER_PHONE_NUMBER = "UserPhoneNumber"
        const val USER_PHOTO_URL = "UserPhotoURL"
        const val EVENT_SIGN_OUT = "SignOut"
    }

    private fun logEvent(eventName: String, params: Bundle) {
        analytics.logEvent(eventName, params)
    }

    fun logButtonClicked(buttonName: String) {
        val params = Bundle().apply {
            putString("button_name", buttonName)
        }
        logEvent("button_clicked", params)
    }

    fun logError(error: String) {
        val params = Bundle().apply {
            putString("error", error)
        }
        logEvent("error", params)
    }

    fun logScreenView(screenName: String) {
        analytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW) {
            param(FirebaseAnalytics.Param.SCREEN_NAME, screenName)
        }
    }

    fun logLogin(user: FirebaseUser) {
        analytics.logEvent(FirebaseAnalytics.Event.LOGIN) {
            param(USER_ID, user.uid)
            param(USER_IS_ANONYMOUS, user.isAnonymous.toString())
            param(USER_DISPLAY_NAME, user.displayName.toString())
            param(USER_EMAIL, user.email.toString())
            param(USER_PHONE_NUMBER, user.phoneNumber.toString())
            param(USER_PHOTO_URL, user.photoUrl.toString())
        }
    }

    fun logSignOut(user: FirebaseUser) {
        analytics.logEvent(EVENT_SIGN_OUT) {
            param(USER_ID, user.uid)
            param(USER_IS_ANONYMOUS, user.isAnonymous.toString())
            param(USER_DISPLAY_NAME, user.displayName.toString())
            param(USER_EMAIL, user.email.toString())
            param(USER_PHONE_NUMBER, user.phoneNumber.toString())
            param(USER_PHOTO_URL, user.photoUrl.toString())
        }
    }

    fun logCreateAccount(user: FirebaseUser) {
        analytics.logEvent(FirebaseAnalytics.Event.SIGN_UP) {
            param(USER_ID, user.uid)
            param(USER_IS_ANONYMOUS, user.isAnonymous.toString())
            param(USER_DISPLAY_NAME, user.displayName.toString())
            param(USER_EMAIL, user.email.toString())
            param(USER_PHONE_NUMBER, user.phoneNumber.toString())
            param(USER_PHOTO_URL, user.photoUrl.toString())
        }
    }
}