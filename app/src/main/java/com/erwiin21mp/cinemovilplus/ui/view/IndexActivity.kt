package com.erwiin21mp.cinemovilplus.ui.view

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.erwiin21mp.cinemovilplus.R
import com.erwiin21mp.cinemovilplus.Win
import com.erwiin21mp.cinemovilplus.core.navigateToLogin
import com.erwiin21mp.cinemovilplus.core.toast
import com.erwiin21mp.cinemovilplus.data.network.AuthGoogle
import com.erwiin21mp.cinemovilplus.data.network.AuthManager
import com.erwiin21mp.cinemovilplus.data.network.DataBaseManager
import com.erwiin21mp.cinemovilplus.databinding.ActivityIndexBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class IndexActivity : AppCompatActivity() {

    private lateinit var binding: ActivityIndexBinding
    private val auth = AuthManager()
    private val win = Win()
    private val database = DataBaseManager()

    private companion object {
        const val BUTTON_CANCEL = "Cancel"
        const val BUTTON_VALE = "Vale"
        const val BUTTON_SIGN_OUT = "SignOut"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityIndexBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnSignOut.setOnClickListener {
            database.logButtonClicked(BUTTON_SIGN_OUT, auth.getCurrentUser())
            logOut()
        }
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

        btnCancel.setOnClickListener {
            database.logButtonClicked(BUTTON_CANCEL, auth.getCurrentUser())
            dialog.dismiss()
        }

        btnVale.setOnClickListener {
            database.logButtonClicked(BUTTON_VALE, auth.getCurrentUser())
            CoroutineScope(Dispatchers.IO).launch {
                database.logSignOut(auth.getCurrentUser())
                auth.signOut()
                AuthGoogle(this@IndexActivity).signOut()
                runOnUiThread {
                    dialog.dismiss()
                    toast(getString(R.string.signOutMessage))
                    navigateToLogin()
                    finish()
                }
            }
        }
    }
}