package com.erwiin21mp.cinemovilplus.ui.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import com.erwiin21mp.cinemovilplus.R
import com.erwiin21mp.cinemovilplus.Win
import com.erwiin21mp.cinemovilplus.core.navigateToLogin
import com.erwiin21mp.cinemovilplus.core.toast
import com.erwiin21mp.cinemovilplus.data.model.AuthRes
import com.erwiin21mp.cinemovilplus.data.network.AnalyticsManager
import com.erwiin21mp.cinemovilplus.data.network.AuthManager
import com.erwiin21mp.cinemovilplus.databinding.ActivityForgotPasswordBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ForgotPasswordActivity : AppCompatActivity() {

    private lateinit var binding: ActivityForgotPasswordBinding
    private lateinit var textWatcherEmail: TextWatcher
    private var isValidEmail = false
    private val win = Win()
    private val auth: AuthManager = AuthManager()
    private lateinit var analytics: AnalyticsManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForgotPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        analytics = AnalyticsManager(this)
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
            when (auth.sendPasswordResetEmail(email)) {
                is AuthRes.Error -> analytics.logError("Error al enviar el correo")
                is AuthRes.Success -> {
                    navigateToLogin()
                    runOnUiThread { toast("Se ha enviado el correo electronico") }
                }
            }
            dialog.dismiss()
        }
    }
}