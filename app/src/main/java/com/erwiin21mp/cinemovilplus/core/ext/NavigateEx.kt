package com.erwiin21mp.cinemovilplus.core.ext

import android.app.Activity
import android.content.Intent
import androidx.fragment.app.Fragment
import com.erwiin21mp.cinemovilplus.ui.view.contentView.ContentViewActivity
import com.erwiin21mp.cinemovilplus.ui.view.forgotPassword.ForgotPasswordActivity
import com.erwiin21mp.cinemovilplus.ui.view.home.HomeViewModel.Companion.ID
import com.erwiin21mp.cinemovilplus.ui.view.index.IndexActivity
import com.erwiin21mp.cinemovilplus.ui.view.login.LoginActivity
import com.erwiin21mp.cinemovilplus.ui.view.signUp.SignUpActivity

fun Activity.navigateToIndex() {
    startActivity(Intent(this, IndexActivity::class.java))
}

fun Activity.navigateToLogin() {
    startActivity(Intent(this, LoginActivity::class.java))
}

fun Fragment.navigateToLogin() {
    startActivity(Intent(context, LoginActivity::class.java))
}

fun Activity.navigateToSignUp() {
    startActivity(Intent(this, SignUpActivity::class.java))
}

fun Activity.navigateToForgotPassword() {
    startActivity(Intent(this, ForgotPasswordActivity::class.java))
}

fun Activity.navigateToContent(id: String) {
    startActivity(Intent(this, ContentViewActivity::class.java).putExtra(ID, id))
}

fun Fragment.navigateToContent(id: String) {
    startActivity(Intent(context, ContentViewActivity::class.java).putExtra(ID, id))
}