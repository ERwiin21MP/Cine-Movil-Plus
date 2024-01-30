package com.erwiin21mp.cinemovilplus.ui.view

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.splashscreen.SplashScreen
import com.erwiin21mp.cinemovilplus.Navigate
import com.erwiin21mp.cinemovilplus.R
import com.erwiin21mp.cinemovilplus.core.isNull
import com.erwiin21mp.cinemovilplus.data.network.AuthManager

@SuppressLint("CustomSplashScreen")
class SplashScreenActivity : AppCompatActivity() {
    private val authManager = AuthManager()
    private val navigate = Navigate()
    private val user = authManager.getCurrentUser()

    override fun onCreate(savedInstanceState: Bundle?) {
        val screenSplash = installSplashScreen()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        initSplash(screenSplash)
    }

    private fun initSplash(screenSplash: SplashScreen) {
        screenSplash.setKeepOnScreenCondition { true }
        if (user.isNull())
            navigate.toLogin(this)
        else
            navigate.toIndex(this)
        finish()
    }
}