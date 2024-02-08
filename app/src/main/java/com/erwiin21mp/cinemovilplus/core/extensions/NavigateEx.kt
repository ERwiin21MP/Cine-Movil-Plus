package com.erwiin21mp.cinemovilplus.core.extensions

import android.app.Activity
import android.content.Intent
import com.erwiin21mp.cinemovilplus.ui.view.forgotPassword.ForgotPasswordActivity
import com.erwiin21mp.cinemovilplus.ui.view.home.HomeActivity
import com.erwiin21mp.cinemovilplus.ui.view.login.LoginActivity
import com.erwiin21mp.cinemovilplus.ui.view.profile.ProfileActivity
import com.erwiin21mp.cinemovilplus.ui.view.search.SearchActivity
import com.erwiin21mp.cinemovilplus.ui.view.signUp.SignUpActivity

fun Activity.navigateToIndex() {
    startActivity(Intent(this, HomeActivity::class.java))
}

fun Activity.navigateToLogin() {
    startActivity(Intent(this, LoginActivity::class.java))
}

fun Activity.navigateToSignUp() {
    startActivity(Intent(this, SignUpActivity::class.java))
}

fun Activity.navigateToForgotPassword() {
    startActivity(Intent(this, ForgotPasswordActivity::class.java))
}

fun Activity.navigateToProfile() {
    startActivity(Intent(this, ProfileActivity::class.java))
}

fun Activity.navigateToSearch() {
    startActivity(Intent(this, SearchActivity::class.java))
}