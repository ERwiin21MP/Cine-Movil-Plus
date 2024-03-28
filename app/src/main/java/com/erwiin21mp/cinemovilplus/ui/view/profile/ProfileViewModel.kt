package com.erwiin21mp.cinemovilplus.ui.view.profile

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.erwiin21mp.cinemovilplus.data.network.firebase.AuthManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(private val auth: AuthManager) : ViewModel() {

    val profilePhotoURL = MutableLiveData<String?>()
    val userName = MutableLiveData<String?>()
    val email = MutableLiveData<String?>()
    val uid = MutableLiveData<String?>()

    init {
        getProfilePhoto()
        getUSerName()
        getEmail()
        getUID()
    }

    private fun getUID() {
        uid.postValue(auth.getCurrentUser()?.uid)
    }

    private fun getEmail() {
        email.postValue(auth.getCurrentUser()?.email)
    }

    private fun getUSerName() {
        userName.postValue(auth.getCurrentUser()?.displayName)
    }

    private fun getProfilePhoto() {
        profilePhotoURL.postValue(auth.getCurrentUser()?.photoUrl.toString())
    }
//    private fun loadUIDProfile() {
//        val user = auth.getCurrentUser()
//        binding.apply {
//            tvUID.text = "UID: ${user!!.uid}"
//
//            var displayName = user.displayName
//            val email = user.email
//
//            if (user.isAnonymous) {
//                displayName = context?.getString(R.string.userAnonymous)
//                tvUserEmail.visibility = View.GONE
//            } else Picasso.get().load(user.photoUrl).error(R.drawable.ic_user)
//                .transform(CropCircleTransformation()).into(ivProfilePhoto)
//            tvUserName.text = displayName
//            tvUserEmail.text = email
//        }
//    }
}