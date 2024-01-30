package com.erwiin21mp.cinemovilplus

import android.annotation.SuppressLint
import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import com.erwiin21mp.cinemovilplus.databinding.ActivityIndexBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class IndexActivity : AppCompatActivity() {

    private lateinit var binding: ActivityIndexBinding
    private val authManager = AuthManager()
    private val analyticsManager = AnalyticsManager()
    private val win = Win()
    private val navigate = Navigate()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityIndexBinding.inflate(layoutInflater)
        setContentView(binding.root)

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
                analyticsManager.logSignOut(authManager.getCurrentUser()!!)
                authManager.signOut()
                AuthGoogle(this@IndexActivity).signOut()
                runOnUiThread {
                    dialog.dismiss()
                    win.toast(this@IndexActivity, "Has cerrado sesión")
                    navigate.toLogin(this@IndexActivity)
                }
            }
        }
    }
}