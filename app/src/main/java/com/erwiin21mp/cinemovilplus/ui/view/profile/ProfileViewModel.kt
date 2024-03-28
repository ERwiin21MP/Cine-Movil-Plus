package com.erwiin21mp.cinemovilplus.ui.view.profile

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.erwiin21mp.cinemovilplus.R
import com.erwiin21mp.cinemovilplus.data.network.firebase.AuthManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val auth: AuthManager,
    private val context: Context
) : ViewModel() {

    val profilePhotoURL = MutableLiveData<String>()
    val userName = MutableLiveData<String>()
    val email = MutableLiveData<String>()
    val uid = MutableLiveData<String>()
    private val user = auth.getCurrentUser()!!

    init {
        getProfilePhoto()
        getUserName()
        getEmail()
        getUID()
    }

    private fun getUID() {
        uid.postValue(user.uid)
    }

    private fun getEmail() {
        if (!user.isAnonymous)
            email.postValue(user.email)
    }

    private fun getUserName() {
        userName.postValue(
            if (user.isAnonymous) context.getString(R.string.userAnonymous)
            else user.displayName ?: context.getString(R.string.unknown)
        )
    }

    private fun getProfilePhoto() {
        profilePhotoURL.postValue(auth.getCurrentUser()!!.photoUrl.toString())
    }
}