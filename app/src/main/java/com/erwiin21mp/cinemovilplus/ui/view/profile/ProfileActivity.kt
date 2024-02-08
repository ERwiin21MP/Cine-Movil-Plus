package com.erwiin21mp.cinemovilplus.ui.view.profile

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import com.erwiin21mp.cinemovilplus.R
import com.erwiin21mp.cinemovilplus.core.extensions.navigateToLogin
import com.erwiin21mp.cinemovilplus.core.extensions.toast
import com.erwiin21mp.cinemovilplus.data.network.AuthGoogle
import com.erwiin21mp.cinemovilplus.data.network.AuthManager
import com.erwiin21mp.cinemovilplus.data.network.DataBaseManager
import com.erwiin21mp.cinemovilplus.databinding.ActivityProfileBinding
import com.erwiin21mp.cinemovilplus.ui.utils.Win
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileBinding
    private val auth = AuthManager()
    private val win = Win()
    private val database = DataBaseManager()
    private lateinit var navController: NavController

    private companion object {
        const val BUTTON_CANCEL = "Cancel"
        const val BUTTON_VALE = "Vale"
        const val BUTTON_SIGN_OUT = "SignOut"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
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
                AuthGoogle(this@ProfileActivity).signOut()
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