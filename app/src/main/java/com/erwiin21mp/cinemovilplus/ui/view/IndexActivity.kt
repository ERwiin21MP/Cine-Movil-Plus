package com.erwiin21mp.cinemovilplus.ui.view

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import com.erwiin21mp.cinemovilplus.R
import com.erwiin21mp.cinemovilplus.Win
import com.erwiin21mp.cinemovilplus.core.navigateToLogin
import com.erwiin21mp.cinemovilplus.core.toast
import com.erwiin21mp.cinemovilplus.data.network.AnalyticsManager
import com.erwiin21mp.cinemovilplus.data.network.AuthGoogle
import com.erwiin21mp.cinemovilplus.data.network.AuthManager
import com.erwiin21mp.cinemovilplus.databinding.ActivityIndexBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class IndexActivity : AppCompatActivity() {

    private lateinit var binding: ActivityIndexBinding
    private val auth = AuthManager()
    private lateinit var analytics:AnalyticsManager
    private val win = Win()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityIndexBinding.inflate(layoutInflater)
        setContentView(binding.root)

        analytics = AnalyticsManager(this)
        binding.btnSignOut.setOnClickListener { logOut() }
    }

    private fun logOut() {
        val view = LayoutInflater.from(this).inflate(R.layout.alert_sign_out, null)
        val btnCancel: Button = view.findViewById(R.id.btnCancel)
        val btnVale: Button = view.findViewById(R.id.btnVale)
        var dialog: Dialog

        AlertDialog.Builder(this, R.style.AlertWithOutBackground).apply {
            setView(view)
            create()
            dialog = show()
        }

        btnCancel.setOnClickListener { dialog.dismiss() }

        btnVale.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                analytics.logSignOut(auth.getCurrentUser()!!)
                auth.signOut()
                AuthGoogle(this@IndexActivity).signOut()
                runOnUiThread {
                    dialog.dismiss()
                    toast("Has cerrado sesión")
                    navigateToLogin()
                }
            }
        }
    }
}