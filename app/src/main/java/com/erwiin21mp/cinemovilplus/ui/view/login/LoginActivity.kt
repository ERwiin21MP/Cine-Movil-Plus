package com.erwiin21mp.cinemovilplus.ui.view.login

import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.appcompat.app.AppCompatActivity
import com.erwiin21mp.cinemovilplus.R
import com.erwiin21mp.cinemovilplus.core.ext.logData
import com.erwiin21mp.cinemovilplus.core.ext.navigateToForgotPassword
import com.erwiin21mp.cinemovilplus.core.ext.navigateToIndex
import com.erwiin21mp.cinemovilplus.core.ext.navigateToSignUp
import com.erwiin21mp.cinemovilplus.core.ext.onTextChanged
import com.erwiin21mp.cinemovilplus.core.ext.toast
import com.erwiin21mp.cinemovilplus.data.model.AuthRes
import com.erwiin21mp.cinemovilplus.data.network.AuthGoogle
import com.erwiin21mp.cinemovilplus.data.network.AuthManager
import com.erwiin21mp.cinemovilplus.data.network.DataBaseManager
import com.erwiin21mp.cinemovilplus.databinding.ActivityLoginBinding
import com.erwiin21mp.cinemovilplus.ui.utils.Win
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private var isValidEmail = false
    private var isValidPassword = false
    private val win = Win()
    private val auth: AuthManager = AuthManager()
    private val dataBase = DataBaseManager()
    private lateinit var authGoogle: AuthGoogle

    private companion object {
        const val BUTTON_LOGIN = "Login"
        const val BUTTON_FORGOT_PASSWORD = "ForgotPassword"
        const val BUTTON_SIGN_GUEST = "Guest"
        const val BUTTON_SIGN_UP = "SignUp"
        const val BUTTON_LOGIN_WITH_GOOGLE = "LoginWithGoogle"
        const val SIGN_CODE = "45"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        authGoogle = AuthGoogle(this)
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
            btnLoginWithGoogle.setOnClickListener {
                dataBase.logButtonClicked(BUTTON_LOGIN_WITH_GOOGLE, auth.getCurrentUser())
                loginWithGoogle()
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

    private fun loginWithGoogle() {
        authGoogle.signOut()
        val googleSignInLauncher =
            activityResultRegistry.register(SIGN_CODE, StartActivityForResult()) { result ->
                when (val account =
                    authGoogle.handleSignInResult(GoogleSignIn.getSignedInAccountFromIntent(result.data))) {
                    is AuthRes.Success -> {
                        val credential =
                            GoogleAuthProvider.getCredential(account.data.idToken, null)
                        CoroutineScope(Dispatchers.IO).launch {
                            val firebaseUser = authGoogle.signInWithGoogleCredential(credential)
                            if (firebaseUser != null) {
                                runOnUiThread { loginSuccess(AuthRes.Success(auth.getCurrentUser())) }
                                finish()
                            }
                        }
                    }

                    is AuthRes.Error -> {
                        logData("ERROR3 ${account.errorMessage}")
                        loginError(AuthRes.Error(account.errorMessage))
                    }
                }
            }
        authGoogle.signInWithGoogle(googleSignInLauncher)
    }

    private fun signGuest() {
        val dialog = win.getAndShowAlertOfWaiting(this, getString(R.string.loggingIn))

        CoroutineScope(Dispatchers.IO).launch {
            when (val result = auth.signAnonymously()) {
                is AuthRes.Error -> loginError(result)
                is AuthRes.Success -> loginSuccess(result)
            }
            dialog.dismiss()
            finish()
        }
    }

    private fun loginError(result: AuthRes.Error) {
        dataBase.logErrorLogin(
            result,
            binding.etEmail.text.toString().trim(),
            binding.etPassword.text.toString()
        )
        runOnUiThread { win.showAlertOfMessage(this) }
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