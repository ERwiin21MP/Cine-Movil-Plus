package com.erwiin21mp.cinemovilplus.ui.view

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import com.erwiin21mp.cinemovilplus.R
import com.erwiin21mp.cinemovilplus.Win
import com.google.firebase.auth.FirebaseUser
import com.erwiin21mp.cinemovilplus.Win.Companion.MIN_PASSWORD_LENGTH
import com.erwiin21mp.cinemovilplus.core.navigateToIndex
import com.erwiin21mp.cinemovilplus.core.navigateToLogin
import com.erwiin21mp.cinemovilplus.core.toast
import com.erwiin21mp.cinemovilplus.data.model.AuthRes
import com.erwiin21mp.cinemovilplus.data.network.AuthManager
import com.erwiin21mp.cinemovilplus.databinding.ActivityRegisterBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var textWatcherUserName: TextWatcher
    private lateinit var textWatcherEmail: TextWatcher
    private lateinit var textWatcherPassword: TextWatcher
    private lateinit var textWatcherConfirmed: TextWatcher
    private var isValidUserName = false
    private var isValidEmail = false
    private var isValidPassword = false
    private var isEqualsPasswords = false
    private val win = Win()
    private val authManager = AuthManager()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initTextWatchers()
        setListeners()
    }

    private fun initTextWatchers() {
        textWatcherUserName = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                var text = s.toString()
                val modifiedText = text.replace(" ", "_")

                if (text != modifiedText) {
                    binding.etUserName.apply {
                        removeTextChangedListener(textWatcherUserName)
                        setText(modifiedText)
                        setSelection(modifiedText.length)
                        addTextChangedListener(textWatcherUserName)
                    }
                    text = modifiedText
                }

                val maxLength = 30
                if (text.length > maxLength) {
                    val truncatedText = text.substring(0, maxLength)
                    binding.etUserName.apply {
                        setText(truncatedText)
                        setSelection(truncatedText.length)
                    }
                }

                isValidUserName = win.isValidUserName(text)

                binding.etUserName.setBackgroundResource(if (isValidUserName) R.drawable.bg_edit_text else R.drawable.bg_edit_text_error)
            }
        }

        textWatcherEmail = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                isValidEmail = win.isValidEmail(s.toString())
                binding.etEmail.setBackgroundResource(if (isValidEmail) R.drawable.bg_edit_text else R.drawable.bg_edit_text_error)
            }
        }

        textWatcherPassword = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                isValidPassword = win.isValidPassword(s.toString())
                binding.etPassword.setBackgroundResource(if (isValidPassword) R.drawable.bg_edit_text else R.drawable.bg_edit_text_error)
            }
        }

        textWatcherConfirmed = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                isEqualsPasswords = s.toString() == binding.etPassword.text.toString()
                val drawable =
                    if (isEqualsPasswords) R.drawable.bg_edit_text else R.drawable.bg_edit_text_error
                binding.apply {
                    etConfirmedPassword.setBackgroundResource(drawable)
                    etPassword.setBackgroundResource(drawable)
                }
            }
        }
    }

    private fun setListeners() {
        binding.apply {
            btnRegister.setOnClickListener { register() }
            etUserName.addTextChangedListener(textWatcherUserName)
            etEmail.addTextChangedListener(textWatcherEmail)
            etPassword.addTextChangedListener(textWatcherPassword)
            etConfirmedPassword.addTextChangedListener(textWatcherConfirmed)
            tvLogin.setOnClickListener {
                navigateToLogin()
                finish()
            }
        }
    }

    private fun register() {
        val userName = binding.etUserName.text.toString().trim()
        val email = binding.etEmail.text.toString().trim()
        val password = binding.etPassword.text.toString()
        val confirmedPassword = binding.etConfirmedPassword.text.toString()

        if (userName.isEmpty())
            win.setError(binding.etUserName, getString(R.string.ingresa_tu_nombre_de_usuario))
        else if (email.isEmpty())
            win.setError(binding.etEmail, getString(R.string.ingresa_tu_correo))
        else if (!win.isValidEmail(email))
            win.setError(binding.etEmail, getString(R.string.ingresa_un_correo_valido))
        else if (password.isEmpty())
            win.setError(binding.etPassword, getString(R.string.ingresa_tu_contraseña))
        else if (password.length < MIN_PASSWORD_LENGTH)
            win.setError(binding.etPassword, getString(R.string.password_length_warning))
        else if (confirmedPassword.isEmpty())
            win.setError(binding.etConfirmedPassword, getString(R.string.ingresa_tu_contraseña))
        else if (password != confirmedPassword)
            win.setError(
                binding.etConfirmedPassword,
                getString(R.string.password_confirmed_warning)
            )
        else {
            win.hideSoftKeyboard(binding.etUserName)
            createAccount(userName, email, password)
        }
    }

    private fun createAccount(userName: String, email: String, password: String) {
        val dialog = win.getAndShowAlertOfWaiting(this, "")
        CoroutineScope(Dispatchers.IO).launch {
            when (val result = authManager.createUserWithEmailAndPassword(email, password)) {
                is AuthRes.Success -> createAccountSuccess(result, userName, dialog)
                is AuthRes.Error -> {}//analytics.logError(result.errorMessage)
            }
        }
    }

    private suspend fun createAccountSuccess(result: AuthRes.Success<FirebaseUser?>, userName: String, dialog: Dialog) {
//        analytics.logLogin(result.data!!)
        when (authManager.updateUserDisplayName(userName)) {
            is AuthRes.Error -> {
//                analytics.logError("No se ha podido actualizar el username")
                runOnUiThread { dialog.dismiss() }
            }

            is AuthRes.Success -> {
                runOnUiThread {
//                    analytics.logCreateAccount(authManager.getCurrentUser()!!)
                    dialog.dismiss()
                    navigateToIndex()
                    toast("Se ha creado la cuenta")
                    finish()
                }
            }
        }
    }
}