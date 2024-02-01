package com.erwiin21mp.cinemovilplus.ui.view

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.splashscreen.SplashScreen
import com.erwiin21mp.cinemovilplus.R
import com.erwiin21mp.cinemovilplus.core.isNull
import com.erwiin21mp.cinemovilplus.core.navigateToIndex
import com.erwiin21mp.cinemovilplus.core.navigateToLogin
import com.erwiin21mp.cinemovilplus.data.network.AuthManager
import com.erwiin21mp.cinemovilplus.data.network.DataBaseManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@SuppressLint("CustomSplashScreen")
class SplashScreenActivity : AppCompatActivity() {

    private val auth = AuthManager()
    private val user = auth.getCurrentUser()
    private val database = DataBaseManager()

    override fun onCreate(savedInstanceState: Bundle?) {
        val screenSplash = installSplashScreen()
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_splash_screen)
        initSplash(screenSplash)
    }

    private fun initSplash(screenSplash: SplashScreen) {
        screenSplash.setKeepOnScreenCondition { true }
        if (user.isNull()) navigateToLogin()
        else {
            CoroutineScope(Dispatchers.IO).launch { database.logAppOpen(user!!) }
            navigateToIndex()
        }
        finish()
    }
}