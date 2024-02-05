package com.erwiin21mp.cinemovilplus.core

import android.app.Activity
import android.content.Intent
import com.erwiin21mp.cinemovilplus.ui.view.Index.IndexActivity
import com.erwiin21mp.cinemovilplus.ui.view.forgotPassword.ForgotPasswordActivity
import com.erwiin21mp.cinemovilplus.ui.view.login.LoginActivity
import com.erwiin21mp.cinemovilplus.ui.view.signUp.RegisterActivity

fun Activity.navigateToIndex() {
    startActivity(Intent(this, IndexActivity::class.java))
}

fun Activity.navigateToLogin() {
    startActivity(Intent(this, LoginActivity::class.java))
}

fun Activity.navigateToSignUp() {
    startActivity(Intent(this, RegisterActivity::class.java))
}

fun Activity.navigateToForgotPassword() {
    startActivity(Intent(this, ForgotPasswordActivity::class.java))
}