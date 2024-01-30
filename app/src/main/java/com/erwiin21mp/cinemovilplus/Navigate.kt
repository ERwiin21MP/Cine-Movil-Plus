package com.erwiin21mp.cinemovilplus

import android.content.Context
import android.content.Intent
import com.erwiin21mp.cinemovilplus.ui.view.ForgotPasswordActivity
import com.erwiin21mp.cinemovilplus.ui.view.IndexActivity
import com.erwiin21mp.cinemovilplus.ui.view.LoginActivity
import com.erwiin21mp.cinemovilplus.ui.view.RegisterActivity

class Navigate {
    fun toIndex(context: Context) {
        context.startActivity(Intent(context, IndexActivity::class.java))
    }

    fun toLogin(context: Context) {
        context.startActivity(Intent(context, LoginActivity::class.java))
    }

    fun tosignUp(context: Context) {
        context.startActivity(Intent(context, RegisterActivity::class.java))
    }

    fun toForgotPassword(context: Context) {
        context.startActivity(Intent(context, ForgotPasswordActivity::class.java))
    }
}