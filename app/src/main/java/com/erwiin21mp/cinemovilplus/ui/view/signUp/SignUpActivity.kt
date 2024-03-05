package com.erwiin21mp.cinemovilplus.ui.view.signUp

import android.app.Dialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.appcompat.app.AppCompatActivity
import com.erwiin21mp.cinemovilplus.R
import com.erwiin21mp.cinemovilplus.core.ext.navigateToIndex
import com.erwiin21mp.cinemovilplus.core.ext.onTextChanged
import com.erwiin21mp.cinemovilplus.core.ext.toast
import com.erwiin21mp.cinemovilplus.data.model.AuthRes
import com.erwiin21mp.cinemovilplus.data.network.firebase.AuthManager
import com.erwiin21mp.cinemovilplus.data.network.firebase.DataBaseManager
import com.erwiin21mp.cinemovilplus.databinding.ActivityRegisterBinding
import com.erwiin21mp.cinemovilplus.ui.utils.Win
import com.erwiin21mp.cinemovilplus.ui.utils.Win.Companion.MIN_PASSWORD_LENGTH
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SignUpActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var textWatcherUserName: TextWatcher
    private var isValidUserName = false
    private var isValidEmail = false
    private var isValidPassword = false
    private var isEqualsPasswords = false
    private lateinit var win: Win
    private val database = DataBaseManager()
    private lateinit var auth: AuthManager

    private companion object {
        const val BUTTON_ARE_YOU_A_ACCOUNT = "AreYouAAccount"
        const val BUTTON_REGISTER = "Register"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        win = Win(this)
        auth = AuthManager(this)

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
    }

    private fun setListeners() {
        binding.apply {
            btnRegister.setOnClickListener {
                database.logButtonClicked(BUTTON_REGISTER, auth.getCurrentUser())
                register()
            }
            etUserName.addTextChangedListener(textWatcherUserName)
            etEmail.onTextChanged {
                isValidEmail = win.isValidEmail(it.toString())
                binding.etEmail.setBackgroundResource(if (isValidEmail) R.drawable.bg_edit_text else R.drawable.bg_edit_text_error)
            }
            etPassword.onTextChanged {
                isValidPassword = win.isValidPassword(it.toString())
                binding.etPassword.setBackgroundResource(if (isValidPassword) R.drawable.bg_edit_text else R.drawable.bg_edit_text_error)
            }
            etConfirmedPassword.onTextChanged {
                isEqualsPasswords = it.toString() == binding.etPassword.text.toString()
                val drawable =
                    if (isEqualsPasswords) R.drawable.bg_edit_text else R.drawable.bg_edit_text_error
                binding.apply {
                    etConfirmedPassword.setBackgroundResource(drawable)
                    etPassword.setBackgroundResource(drawable)
                }
            }
            tvLogin.setOnClickListener {
                database.logButtonClicked(BUTTON_ARE_YOU_A_ACCOUNT, auth.getCurrentUser())
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
            win.setError(binding.etUserName, getString(R.string.enterYourUsername))
        else if (email.isEmpty())
            win.setError(binding.etEmail, getString(R.string.enterYourEmail))
        else if (!win.isValidEmail(email))
            win.setError(binding.etEmail, getString(R.string.enterAValidEmail))
        else if (password.isEmpty())
            win.setError(binding.etPassword, getString(R.string.enterYourPassword))
        else if (password.length < MIN_PASSWORD_LENGTH)
            win.setError(binding.etPassword, getString(R.string.passwordLengthWarning))
        else if (confirmedPassword.isEmpty())
            win.setError(binding.etConfirmedPassword, getString(R.string.enterYourPassword))
        else if (password != confirmedPassword)
            win.setError(binding.etConfirmedPassword, getString(R.string.passwordConfirmedWarning))
        else {
            win.hideSoftKeyboard(binding.etUserName)
            createAccount(userName, email, password)
        }
    }

    private fun createAccount(userName: String, email: String, password: String) {
        val dialog = win.getAndShowAlertOfWaiting(this, getString(R.string.registeringUser))
        CoroutineScope(Dispatchers.IO).launch {
            when (val result = auth.createUserWithEmailAndPassword(email, password)) {
                is AuthRes.Success -> createAccountSuccess(result, userName, dialog)
                is AuthRes.Error -> database.logErrorCreateAccount(
                    result,
                    userName,
                    email,
                    password
                )
            }
        }
    }

    private suspend fun createAccountSuccess(
        result: AuthRes.Success<FirebaseUser?>,
        userName: String,
        dialog: Dialog
    ) {
        database.logSuccessLogin(result)
        when (val result2 = auth.updateUserDisplayName(userName)) {
            is AuthRes.Error -> {
                database.logErrorUpdateUserDisplayName(userName, result2, result)
                runOnUiThread { dialog.dismiss() }
            }

            is AuthRes.Success -> {
                runOnUiThread {
                    database.logSuccessCreateAccount(result2)
                    dialog.dismiss()
                    navigateToIndex()
                    toast(getString(R.string.createAccountSuccessfullyMessage))
                    finish()
                }
            }
        }
    }
} // 188 lineas -> 168 lineas