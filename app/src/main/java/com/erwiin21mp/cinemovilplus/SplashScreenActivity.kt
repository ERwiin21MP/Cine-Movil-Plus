package com.erwiin21mp.cinemovilplus

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.splashscreen.SplashScreen

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
        if (user == null) navigate.toLogin(this)
        else navigate.toIndex(this)
        finish()
    }
}