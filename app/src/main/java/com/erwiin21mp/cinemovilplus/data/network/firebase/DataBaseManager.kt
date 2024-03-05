package com.erwiin21mp.cinemovilplus.data.network.firebase

import android.annotation.SuppressLint
import com.erwiin21mp.cinemovilplus.core.ext.isNull
import com.erwiin21mp.cinemovilplus.data.model.AuthRes
import com.erwiin21mp.cinemovilplus.ui.view.home.HomeViewModel.Companion.NAME
import com.erwiin21mp.cinemovilplus.ui.view.home.HomeViewModel.Companion.URL
import com.erwiin21mp.cinemovilplus.ui.view.home.HomeViewModel.Companion.VERTICAL_IMAGE_URL
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.Date
import javax.inject.Inject

class DataBaseManager @Inject constructor() {

    private val database: DatabaseReference = FirebaseDatabase.getInstance().reference

    companion object {
        const val CHILD_LOG_APP_OPEN = "LogAppOpen"
        const val CHILD_LOG_ERROR_SEND_EMAIL = "LogErrorSendEmail"
        const val CHILD_LOG_ERROR_CREATE_ACCOUNT = "LogErrorCreateAccount"
        const val CHILD_LOG_ERROR_UPDATE_DISPLAY_NAME = "LogErrorUpdateDisplayName"
        const val CHILD_LOG_SUCCESS_CREATE_ACCOUNT = "LogSuccessCreateAccount"
        const val CHILD_LOG_SIGN_OUT = "LogSignOut"
        const val CHILD_LOG_ERROR_LOGIN = "LogErrorLogin"
        const val CHILD_LOG_SUCCESS_LOGIN = "LogSuccessLogin"
        const val CHILD_LOG_BUTTON_CLICKED = "LogButtonClicked"
        const val CHILD_LOG_ERROR_LOAD_POSTER_IMAGE_CONTENT_VERTICAL =
            "LogErrorLoadPosterImageContentVertical"
        const val CHILD_LO_ERROR_LOAD_IMAGE_PLATFORM = "LogErrorLoadImagePlatform"
        const val UID = "Uid"
        const val IS_ANONYMOUS = "IsAnonymous"
        const val DISPLAY_NAME = "DisplayName"
        const val EMAIL = "Email"
        const val PHONE_NUMBER = "PhoneNumber"
        const val PHOTO_URL = "PhotoURL"
        const val DATE = "Date"
        const val ERROR_MESSAGE = "ErrorMessage"
        const val PASSWORD = "Password"
        const val BUTTON_NAME = "ButtonName"
        const val ID_CONTENT = "IdContent"
    }

    fun logAppOpen(user: FirebaseUser) {
        database.child(CHILD_LOG_APP_OPEN).child(getCleanId("${user.displayName} - ${user.email}"))
            .child(getCurrentDateAndHour()).setValue(mapOf(DATE to getCurrentDateAndHour()))
    }

    fun logErrorLogin(errorMessage: AuthRes.Error, email: String, password: String) {
        val map = mapOf(
            ERROR_MESSAGE to errorMessage,
            EMAIL to email,
            PASSWORD to password,
            DATE to getCurrentDateAndHour()
        )
        database.child(CHILD_LOG_ERROR_LOGIN).push().setValue(map)
    }

    fun logSuccessLogin(user: AuthRes.Success<FirebaseUser?>) {
        database.child(CHILD_LOG_SUCCESS_LOGIN)
            .child(getCleanId("${user.data?.displayName} - ${user.data?.email}"))
            .child(getCurrentDateAndHour()).setValue(mapOf(DATE to getCurrentDateAndHour()))
    }

    fun logButtonClicked(buttonName: String, user: FirebaseUser?) {
        if (!user.isNull()) database.child(CHILD_LOG_BUTTON_CLICKED).child(buttonName)
            .child(getCleanId("${user!!.displayName} - ${user.email}"))
            .child(getCurrentDateAndHour()).setValue(
                mapOf(DATE to getCurrentDateAndHour(), BUTTON_NAME to buttonName)
            )
        else database.child(CHILD_LOG_BUTTON_CLICKED).child(buttonName)
            .child(getCurrentDateAndHour())
            .setValue(mapOf(DATE to getCurrentDateAndHour(), BUTTON_NAME to buttonName))
    }

    fun logErrorSendEmail(result: AuthRes.Error, email: String) {
        val map = mapOf(ERROR_MESSAGE to result, EMAIL to email, DATE to getCurrentDateAndHour())
        database.child(CHILD_LOG_ERROR_SEND_EMAIL).push().setValue(map)
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

    fun logErrorCreateAccount(
        result: AuthRes.Error,
        userName: String,
        email: String,
        password: String
    ) {
        val map = mapOf(
            ERROR_MESSAGE to result,
            DATE to getCurrentDateAndHour(),
            DISPLAY_NAME to userName,
            EMAIL to email,
            PASSWORD to password
        )
        database.child(CHILD_LOG_ERROR_CREATE_ACCOUNT).push().setValue(map)
    }

    fun logErrorUpdateUserDisplayName(
        userName: String,
        error: AuthRes.Error,
        user: AuthRes.Success<FirebaseUser?>
    ) {
        val map = mapOf(
            DATE to getCurrentDateAndHour(),
            DISPLAY_NAME to userName,
            ERROR_MESSAGE to error,
            EMAIL to user.data!!.email
        )
        database.child(CHILD_LOG_ERROR_UPDATE_DISPLAY_NAME).push().setValue(map)
    }

    fun logSuccessCreateAccount(user: AuthRes.Success<FirebaseUser>) {
        val map = getMapUser(user.data)
        database.child(CHILD_LOG_SUCCESS_CREATE_ACCOUNT).push().setValue(map)
    }

    fun logSignOut(user: FirebaseUser?) {
        database.child(CHILD_LOG_SIGN_OUT)
            .child(getCleanId("${user!!.displayName} - ${user.email}"))
            .child(getCurrentDateAndHour()).setValue(mapOf(DATE to getCurrentDateAndHour()))
    }

    fun logErrorLoadPosterImageContentVertical(
        message: Exception?,
        idContent: Int,
        verticalImageUlr: String
    ) {
        val map = mapOf(
            ERROR_MESSAGE to message.toString(),
            ID_CONTENT to idContent,
            VERTICAL_IMAGE_URL to verticalImageUlr
        )
        database.child(CHILD_LOG_ERROR_LOAD_POSTER_IMAGE_CONTENT_VERTICAL).push().setValue(map)
    }

    fun logErrorLoadImagePlatform(message: Exception?, name: String, url: String) {
        val map = mapOf(ERROR_MESSAGE to message.toString(), NAME to name, URL to url)
        database.child(CHILD_LO_ERROR_LOAD_IMAGE_PLATFORM).push().setValue(map)
    }
}