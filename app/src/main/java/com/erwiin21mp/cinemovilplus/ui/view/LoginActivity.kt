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
import com.erwiin21mp.cinemovilplus.data.network.DataBaseManager
import com.erwiin21mp.cinemovilplus.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private var isValidEmail = false
    private var isValidPassword = false
    private val win = Win()
    private val auth: AuthManager = AuthManager()
    private val dataBase = DataBaseManager()

    private companion object {
        const val BUTTON_LOGIN = "Login"
        const val BUTTON_FORGOT_PASSWORD = "ForgotPassword"
        const val BUTTON_SIGN_GUEST = "Guest"
        const val BUTTON_SIGN_UP = "SignUp"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setListeners()
    }

    private fun setListeners() {
        binding.apply {
            btnLogin.setOnClickListener {
                dataBase.logButtonClicked(BUTTON_LOGIN, auth.getCurrentUser())
                login()
            }
            tvForgotPassword.setOnClickListener {
                dataBase.logButtonClicked(BUTTON_FORGOT_PASSWORD, auth.getCurrentUser())
                navigateToForgotPassword()
            }
            tvSignUp.setOnClickListener {
                dataBase.logButtonClicked(BUTTON_SIGN_UP, auth.getCurrentUser())
                navigateToSignUp()
            }
            btnGuest.setOnClickListener {
                dataBase.logButtonClicked(BUTTON_SIGN_GUEST, auth.getCurrentUser())
                signGuest()
            }
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
        val dialog = win.getAndShowAlertOfWaiting(this, getString(R.string.loggingIn))

        CoroutineScope(Dispatchers.IO).launch {
            when (val result = auth.signAnonymously()) {
                is AuthRes.Error -> loginError(result)
                is AuthRes.Success -> loginSuccess(result)
            }
            dialog.dismiss()
        }
    }

    private fun loginError(result: AuthRes.Error) {
        dataBase.logErrorLogin(
            result,
            binding.etEmail.text.toString().trim(),
            binding.etPassword.text.toString()
        )
        runOnUiThread {
            win.showAlertOfMessage(
                this,
                getString(R.string.loginErrorTitle),
                getString(R.string.login_error_message),
                true
            )
        }
    }

    private fun loginSuccess(result: AuthRes.Success<FirebaseUser?>) {
        dataBase.logSuccessLogin(result)
        runOnUiThread { toast(getString(R.string.youHaveSuccessfullyLoggedIn)) }
        navigateToIndex()
    }

    private fun login() {
        val email = binding.etEmail.text.toString().trim()
        val password = binding.etPassword.text.toString()

        if (email.isEmpty())
            win.setError(binding.etEmail, getString(R.string.enterYourEmail))
        else if (!win.isValidEmail(email))
            win.setError(binding.etEmail, getString(R.string.enterAValidEmail))
        else if (password.isEmpty())
            win.setError(binding.etPassword, getString(R.string.enterYourPassword))
        else signWithEmailAndPassword(email, password)
    }

    private fun signWithEmailAndPassword(email: String, password: String) {
        val dialog = win.getAndShowAlertOfWaiting(this, getString(R.string.loggingIn))

        CoroutineScope(Dispatchers.IO).launch {
            when (val result = auth.signInWithEmailAndPassword(email, password)) {
                is AuthRes.Error -> loginError(result)
                is AuthRes.Success -> loginSuccess(result)
            }
            dialog.dismiss()
        }
    }
} // 175 lines -> 125 lines