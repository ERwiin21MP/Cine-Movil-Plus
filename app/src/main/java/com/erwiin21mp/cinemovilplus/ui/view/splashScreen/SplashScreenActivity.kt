package com.erwiin21mp.cinemovilplus.ui.view.splashScreen

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.erwiin21mp.cinemovilplus.R
import com.erwiin21mp.cinemovilplus.core.ext.isNull
import com.erwiin21mp.cinemovilplus.core.ext.navigateToContent
import com.erwiin21mp.cinemovilplus.core.ext.navigateToIndex
import com.erwiin21mp.cinemovilplus.core.ext.navigateToLogin
import com.erwiin21mp.cinemovilplus.data.network.firebase.AuthManager
import com.erwiin21mp.cinemovilplus.data.network.firebase.FirestoreManager
import com.erwiin21mp.cinemovilplus.data.network.firebase.FirestoreManager.Companion.ID
import com.erwiin21mp.cinemovilplus.data.network.firebase.LogDataBaseManager
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@SuppressLint("CustomSplashScreen")
@AndroidEntryPoint
class SplashScreenActivity : AppCompatActivity() {

    @Inject
    lateinit var auth: AuthManager

    @Inject
    lateinit var database: LogDataBaseManager

    @Inject
    lateinit var db: FirestoreManager

    override fun onCreate(savedInstanceState: Bundle?) {
        val screenSplash = installSplashScreen()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        initUI(screenSplash)
    }

    private fun initUI(screenSplash: SplashScreen) {
        CoroutineScope(Dispatchers.IO).launch { database.logAppOpen(auth.getCurrentUser()!!) }
        initSplash(screenSplash)
    }

    private fun initSplash(screenSplash: SplashScreen) {
        screenSplash.setKeepOnScreenCondition { true }
//        askNotificationPermission()

        val id = intent.getStringExtra(ID)

        if (auth.getCurrentUser().isNull()) {
            navigateToLogin()
        } else {
            CoroutineScope(Dispatchers.IO).launch { db.pushUserOpenApp() }
            if (id.isNull()) {
                navigateToIndex()
            } else {
                navigateToContent(id!!)
            }
        }

        finish()
    }

    private fun askNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) ==
                PackageManager.PERMISSION_GRANTED
            ) {

            } else {
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission(),
    ) { isGranted: Boolean ->
        if (isGranted) {

        } else {

        }
    }

    private fun tokenNew() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w("FCM TOKEN", "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }
            val token = task.result
            Log.d("FCM TOKEN", token.toString())
        })
    }
}