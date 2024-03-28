package com.erwiin21mp.cinemovilplus.ui.view.profile

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.erwiin21mp.cinemovilplus.R
import com.erwiin21mp.cinemovilplus.core.ext.logList
import com.erwiin21mp.cinemovilplus.data.network.firebase.AuthManager
import com.erwiin21mp.cinemovilplus.domain.model.LogAppOpenModel
import com.erwiin21mp.cinemovilplus.domain.usecase.GetLogAppOpenUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val auth: AuthManager,
    private val context: Context,
    private val getLogAppOpenUseCase: GetLogAppOpenUseCase
) : ViewModel() {

    private val user = auth.getCurrentUser()!!
    val profilePhotoURL = MutableLiveData<String>()
    val userName = MutableLiveData<String>()
    val email = MutableLiveData<String>()
    val uid = MutableLiveData<String>()
    val listLogAppOpen = MutableLiveData<List<LogAppOpenModel>>(emptyList())

    init {
        getProfilePhoto()
        getUserName()
        getEmail()
        getUID()
        getLogAppOpenList()
    }

    private fun getLogAppOpenList() {
        viewModelScope.launch {
            val list = withContext(Dispatchers.IO) { getLogAppOpenUseCase(user.uid) }
            logList(list, "LOGAPP")
        }
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