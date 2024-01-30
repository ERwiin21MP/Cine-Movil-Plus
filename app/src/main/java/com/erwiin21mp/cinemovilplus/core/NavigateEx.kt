package com.erwiin21mp.cinemovilplus.core

import android.app.Activity
import android.content.Context
import android.content.Intent
import com.erwiin21mp.cinemovilplus.ui.view.ForgotPasswordActivity
import com.erwiin21mp.cinemovilplus.ui.view.IndexActivity
import com.erwiin21mp.cinemovilplus.ui.view.LoginActivity
import com.erwiin21mp.cinemovilplus.ui.view.RegisterActivity

fun Activity.navigateToIndex() { this.startActivity(Intent(this, IndexActivity::class.java)) }
fun Activity.navigateToLogin() { this.startActivity(Intent(this, LoginActivity::class.java)) }
fun Activity.navigateToSignUp() { this.startActivity(Intent(this, RegisterActivity::class.java)) }
fun Activity.navigateToForgotPassword() { this.startActivity(Intent(this, ForgotPasswordActivity::class.java)) }