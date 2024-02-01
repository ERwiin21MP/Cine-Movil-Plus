package com.erwiin21mp.cinemovilplus.ui.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.erwiin21mp.cinemovilplus.R
import com.erwiin21mp.cinemovilplus.Win
import com.erwiin21mp.cinemovilplus.core.navigateToForgotPassword
import com.erwiin21mp.cinemovilplus.core.navigateToIndex
import com.erwiin21mp.cinemovilplus.core.navigateToSignUp
import com.erwiin21mp.cinemovilplus.core.onTextChanged
import com.erwiin21mp.cinemovilplus.core.toast
import com.erwiin21mp.cinemovilplus.data.model.AuthRes
import com.erwiin21mp.cinemovilplus.data.network.AuthManager
import com.google.firebase.auth.FirebaseUser
import com.erwiin21mp.cinemovilplus.databinding.ActivityLoginBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private var isValidEmail = false
    private var isValidPassword = false
    private val win = Win()
    private val auth: AuthManager = AuthManager()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setListeners()
        win.showAlertOfWaiting(
            this,
            "Parametro",
            "Este es el mensaje que se esta enviando desde parametro"
        )
    }

    private fun setListeners() {
        binding.apply {
            btnLogin.setOnClickListener { login() }
            tvForgotPassword.setOnClickListener { forgotPassword() }
            tvSignUp.setOnClickListener { signUp() }
            btnGuest.setOnClickListener { signGuest() }

            etEmail.onTextChanged {
                isValidEmail = win.isValidEmail(it)
                etEmail.setBackgroundResource(if (isValidEmail) R.drawable.bg_edit_text else R.drawable.bg_edit_text_error)
            }
            etPassword.onTextChanged {
                isValidPassword = win.isValidPassword(it)
                etPassword.setBackgroundResource(if (isValidPassword) R.drawable.bg_edit_text else R.drawable.bg_edit_text_error)
            }
        }
    }

    private fun signGuest() {
        val dialog = win.showAlertOfWaiting(this, "", "")

        CoroutineScope(Dispatchers.IO).launch {
            when (val result = auth.signAnonymously()) {
                is AuthRes.Error -> {}//analytics.logError(result.errorMessage)
                is AuthRes.Success -> loginSuccess(result)
            }
            dialog.dismiss()
        }
    }

    private fun loginSuccess(result: AuthRes.Success<FirebaseUser>) {
//        analytics.logLogin(result.data)
        navigateToIndex()
        runOnUiThread { toast("Has iniciado sesión") }
    }

    private fun signUp() {
        navigateToSignUp()
    }

    private fun forgotPassword() {
        navigateToForgotPassword()
    }

    private fun login() {
        val email = binding.etEmail.text.toString().trim()
        val password = binding.etPassword.text.toString()

        if (email.isEmpty())
            win.setError(binding.etEmail, getString(R.string.ingresa_tu_correo))
        else if (!win.isValidEmail(email))
            win.setError(binding.etEmail, getString(R.string.ingresa_un_correo_valido))
        else if (password.isEmpty())
            win.setError(binding.etPassword, getString(R.string.ingresa_tu_contraseña))
        else signWithEmailAndPassword(email, password)
    }

    private fun signWithEmailAndPassword(email: String, password: String) {
        val dialog = win.showAlertOfWaiting(this, "", "")
        CoroutineScope(Dispatchers.IO).launch {
            when (val result = auth.signInWithEmailAndPassword(email, password)) {
                is AuthRes.Error -> {}//analytics.logError("Error al iniciar sesión")
                is AuthRes.Success -> {
//                    analytics.logLogin(result.data!!)
                    runOnUiThread {
                        navigateToIndex()
                        toast("Has iniciado sesión")
                    }
                }
            }
            runOnUiThread { dialog.dismiss() }
        }
    }
} // 175 lineas