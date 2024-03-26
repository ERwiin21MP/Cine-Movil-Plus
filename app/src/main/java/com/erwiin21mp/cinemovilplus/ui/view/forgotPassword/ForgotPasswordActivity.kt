package com.erwiin21mp.cinemovilplus.ui.view.forgotPassword

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.erwiin21mp.cinemovilplus.R
import com.erwiin21mp.cinemovilplus.core.ext.onTextChanged
import com.erwiin21mp.cinemovilplus.data.model.AuthRes
import com.erwiin21mp.cinemovilplus.data.network.firebase.AuthManager
import com.erwiin21mp.cinemovilplus.data.network.firebase.LogDataBaseManager
import com.erwiin21mp.cinemovilplus.databinding.ActivityForgotPasswordBinding
import com.erwiin21mp.cinemovilplus.ui.utils.Win
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ForgotPasswordActivity : AppCompatActivity() {

    private lateinit var binding: ActivityForgotPasswordBinding
    private var isValidEmail = false
    private lateinit var win: Win
    private lateinit var auth: AuthManager
    private val database = LogDataBaseManager()

    private companion object {
        const val BUTTON_SEND_EMAIL = "SendEmail"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForgotPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        win = Win(this)
        auth = AuthManager(this)
        setListeners()
    }

    private fun setListeners() {
        binding.apply {
            etEmail.onTextChanged {
                isValidEmail = win.isValidEmail(it.toString())
                etEmail.setBackgroundResource(if (isValidEmail) R.drawable.bg_edit_text else R.drawable.bg_edit_text_error)
            }
            btnSendEmail.setOnClickListener {
                database.logButtonClicked(BUTTON_SEND_EMAIL, auth.getCurrentUser())
                sendEmail()
            }
        }
    }

    private fun sendEmail() {
        val email = binding.etEmail.text.toString().trim()

        if (email.isEmpty())
            win.setError(binding.etEmail, getString(R.string.enterYourEmail))
        else if (!win.isValidEmail(email))
            win.setError(binding.etEmail, getString(R.string.enterAValidEmail))
        else sendEmail(email)
    }

    private fun sendEmail(email: String) {
        val dialog =
            win.getAndShowAlertOfWaiting(this, getString(R.string.sendingPasswordResetEmail))
        CoroutineScope(Dispatchers.IO).launch {
            when (val result = auth.sendPasswordResetEmail(email)) {
                is AuthRes.Error -> database.logErrorSendEmail(
                    result,
                    binding.etEmail.text.toString()
                )

                is AuthRes.Success -> {
                    runOnUiThread {
                        dialog.dismiss()
                        win.showAlertOfResetEmailSent(this@ForgotPasswordActivity) { finish() }
                    }
                }
            }
        }
    }
}