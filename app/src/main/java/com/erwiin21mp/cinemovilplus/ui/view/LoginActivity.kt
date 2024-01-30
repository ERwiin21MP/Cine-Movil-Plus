package com.erwiin21mp.cinemovilplus.ui.view

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.erwiin21mp.cinemovilplus.R
import com.erwiin21mp.cinemovilplus.Win
import com.erwiin21mp.cinemovilplus.core.navigateToForgotPassword
import com.erwiin21mp.cinemovilplus.core.navigateToIndex
import com.erwiin21mp.cinemovilplus.core.navigateToSignUp
import com.erwiin21mp.cinemovilplus.core.toast
import com.erwiin21mp.cinemovilplus.data.model.AuthRes
import com.erwiin21mp.cinemovilplus.data.network.AnalyticsManager
import com.erwiin21mp.cinemovilplus.data.network.AuthGoogle
import com.erwiin21mp.cinemovilplus.data.network.AuthManager
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.erwiin21mp.cinemovilplus.databinding.ActivityLoginBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {

    private companion object {
        const val BUTTON_SIGN_ANONYMOUS = "SignAnonymous"
        const val SIGN_CODE = "45"
    }

    private lateinit var binding: ActivityLoginBinding
    private lateinit var textWatcherEmail: TextWatcher
    private lateinit var textWatcherPassword: TextWatcher
    private var isValidEmail = false
    private var isValidPassword = false
    private val win = Win()
    private val auth: AuthManager = AuthManager()
    private val analyticsManager: AnalyticsManager = AnalyticsManager()
    private lateinit var authGoogle: AuthGoogle
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initTextWatchers()
        setListeners()
        authGoogle = AuthGoogle(this)
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

        textWatcherPassword = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                isValidPassword = win.isValidPassword(s.toString())
                binding.etPassword.setBackgroundResource(if (isValidPassword) R.drawable.bg_edit_text else R.drawable.bg_edit_text_error)
            }
        }
    }

    private fun setListeners() {
        binding.apply {
            btnLogin.setOnClickListener { login() }
            btnLoginWithGoogle.setOnClickListener { loginWithGoogle() }
            tvForgotPassword.setOnClickListener { forgotPassword() }
            tvSignUp.setOnClickListener { signUp() }
            tvSignAnnonymous.setOnClickListener {
                analyticsManager.logButtonClicked(BUTTON_SIGN_ANONYMOUS)
                signAnonymous()
            }

            etEmail.addTextChangedListener(textWatcherEmail)
            etPassword.addTextChangedListener(textWatcherPassword)
        }
    }

    private fun loginWithGoogle() {
        val googleSignInLauncher = activityResultRegistry.register(
            SIGN_CODE,
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            when (val account =
                authGoogle.handleSignInResult(GoogleSignIn.getSignedInAccountFromIntent(result.data))) {
                is AuthRes.Success -> {
                    val credential = GoogleAuthProvider.getCredential(account.data.idToken, null)
                    CoroutineScope(Dispatchers.IO).launch {
                        val firebaseUser = authGoogle.signInWithGoogleCredential(credential)
                        if (firebaseUser != null) {
                            navigateToIndex()
                            finish()
                            runOnUiThread { toast("Has iniciado sesión") }
                        }
                    }
                }

                is AuthRes.Error -> {
                    Log.e("ERROR3", account.errorMessage)
                    toast("Error")
                }
            }
        }
        authGoogle.signInWithGoogle(googleSignInLauncher)
    }

    private fun signAnonymous() {
        val dialog = win.showAlertOfWaiting(this, R.layout.alert_iniciando_sesion)

        CoroutineScope(Dispatchers.IO).launch {
            when (val result = auth.signAnonymously()) {
                is AuthRes.Error -> analyticsManager.logError(result.errorMessage)
                is AuthRes.Success -> loginSuccess(result)
            }
            dialog.dismiss()
        }
    }

    private fun loginSuccess(result: AuthRes.Success<FirebaseUser>) {
        analyticsManager.logLogin(result.data)
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
        val dialog = win.showAlertOfWaiting(this, R.layout.alert_iniciando_sesion)
        CoroutineScope(Dispatchers.IO).launch {
            when (val result = auth.signInWithEmailAndPassword(email, password)) {
                is AuthRes.Error -> analyticsManager.logError("Error al iniciar sesión")
                is AuthRes.Success -> {
                    analyticsManager.logLogin(result.data!!)
                    runOnUiThread {
                        navigateToIndex()
                        toast("Has iniciado sesión")
                    }
                }
            }
            runOnUiThread { dialog.dismiss() }
        }
    }
}