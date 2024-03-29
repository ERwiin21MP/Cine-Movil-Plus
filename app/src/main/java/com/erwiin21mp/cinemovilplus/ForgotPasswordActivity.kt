package com.erwiin21mp.cinemovilplus

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import com.erwiin21mp.cinemovilplus.databinding.ActivityForgotPasswordBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ForgotPasswordActivity : AppCompatActivity() {

    private lateinit var binding: ActivityForgotPasswordBinding
    private lateinit var textWatcherEmail: TextWatcher
    private var isValidEmail = false
    private val win = Win()
    private val authManager: AuthManager = AuthManager()
    private val analyticsManager: AnalyticsManager = AnalyticsManager()
    private val navigate = Navigate()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForgotPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initTextWatchers()
        setListeners()
    }

    private fun initTextWatchers() {
        textWatcherEmail = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                isValidEmail = win.isValidEmail(s.toString())
                binding.etEmail.setBackgroundResource(if (isValidEmail) R.drawable.bg_edit_text else R.drawable.bg_edit_text_error)
            }
        }
    }

    private fun setListeners() {
        binding.apply {
            etEmail.addTextChangedListener(textWatcherEmail)
            btnSendEmail.setOnClickListener { sendEmail() }
        }
    }

    private fun sendEmail() {
        val email = binding.etEmail.text.toString().trim()

        if (email.isEmpty())
            win.setError(binding.etEmail, getString(R.string.ingresa_tu_correo))
        else if (!win.isValidEmail(email))
            win.setError(binding.etEmail, getString(R.string.ingresa_un_correo_valido))
        else sendEmail(email)
    }

    private fun sendEmail(email: String) {
        val dialog = win.showAlertOfWaiting(this, R.layout.alert_enviando_correo)
        CoroutineScope(Dispatchers.IO).launch {
            when (authManager.sendPasswordResetEmail(email)) {
                is AuthRes.Error -> analyticsManager.logError("Error al enviar el correo")
                is AuthRes.Success -> {
                    navigate.toLogin(this@ForgotPasswordActivity)
                    runOnUiThread { win.toast(this@ForgotPasswordActivity, "Se ha enviado el correo electronico") }
                }
            }
            dialog.dismiss()
        }
    }
}