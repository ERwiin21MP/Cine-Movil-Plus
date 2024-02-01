package com.erwiin21mp.cinemovilplus.data.network

import android.annotation.SuppressLint
import com.erwiin21mp.cinemovilplus.data.model.AuthRes
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.tasks.await
import java.text.SimpleDateFormat
import java.util.Date

class DataBaseManager {

    private val db: DatabaseReference = FirebaseDatabase.getInstance().reference

    private companion object {
        const val CHILD_LOG_APP_OPEN = "LogAppOpen"
        const val UID = "Uid"
        const val IS_ANONYMOUS = "IsAnonymous"
        const val DISPLAY_NAME = "DisplayName"
        const val EMAIL = "Email"
        const val PHONE_NUMBER = "PhoneNumber"
        const val PHOTO_URL = "PhotoURL"
        const val DATE = "Date"
        const val CHILD_LOG_ERROR_LOGIN = "LogErrorLogin"
        const val CHILD_LOG_SUCCESS_LOGIN = "LogSuccessLogin"
    }

    fun logAppOpen(user: FirebaseUser) {
        db.child(CHILD_LOG_APP_OPEN).child(getCleanId("${user.displayName} - ${user.email}"))
            .child(getCurrentDateAndHour()).setValue(mapOf(DATE to getCurrentDateAndHour()))
    }

    fun logErrorLogin(errorMessage: AuthRes.Error) {
        db.child(CHILD_LOG_ERROR_LOGIN).push().setValue(errorMessage)
    }

    fun logSuccessLogin(user: AuthRes.Success<FirebaseUser>) {
        db.child(CHILD_LOG_SUCCESS_LOGIN)
            .child(getCleanId("${user.data.displayName} - ${user.data.email}"))
            .child(getCurrentDateAndHour()).setValue(mapOf(DATE to getCurrentDateAndHour()))
    }

    private fun getCleanId(id: String) =
        id.replace(".", "").replace("#", "").replace("\$", "").replace("[", "").replace("]", "")

    private fun getMapUser(user: FirebaseUser) = mapOf(
        UID to user.uid,
        IS_ANONYMOUS to user.isAnonymous.toString(),
        DISPLAY_NAME to user.displayName.toString(),
        EMAIL to user.email.toString(),
        PHONE_NUMBER to user.phoneNumber.toString(),
        PHOTO_URL to user.photoUrl.toString(),
        DATE to getCurrentDateAndHour()
    )

    @SuppressLint("SimpleDateFormat")
    fun getCurrentDateAndHour() =
        SimpleDateFormat("d MMM yyyy, EEE, HH:mm:ss z").format(Date()).toString()
}