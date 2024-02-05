package com.erwiin21mp.cinemovilplus.data.network

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import com.erwiin21mp.cinemovilplus.R
import com.erwiin21mp.cinemovilplus.data.model.AuthRes
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

class AuthGoogle(context: Context) {

    private val signInClient = Identity.getSignInClient(context)
    fun handleSignInResult(task: Task<GoogleSignInAccount>): AuthRes<GoogleSignInAccount> {
        return try {
            val account = task.getResult(ApiException::class.java)
            AuthRes.Success(account)
        } catch (e: Exception) {
            Log.e("ERROR1", e.message.toString())
            AuthRes.Error(e.message ?: "Google sign-in failed")
        }
    }

    private val auth: FirebaseAuth by lazy { Firebase.auth }

    private val googleSignInClient: GoogleSignInClient by lazy {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(context.getString(R.string.api_key)).requestEmail().build()
        GoogleSignIn.getClient(context, gso)
    }

    suspend fun signInWithGoogleCredential(credential: AuthCredential): AuthRes<FirebaseUser>? {
        return try {
            val firebaseUser = auth.signInWithCredential(credential).await()
            firebaseUser.user?.let { AuthRes.Success(it) } ?: throw Exception("Sign in with Google failed")
        } catch (e: Exception) {
            Log.e("ERROR2", e.message.toString())
            AuthRes.Error(e.message ?: "Sign in with Google failed")
        }
    }

    fun signInWithGoogle(googleSignInLauncher: ActivityResultLauncher<Intent>) {
        val signInIntent = googleSignInClient.signInIntent
        googleSignInLauncher.launch(signInIntent)
    }

    fun signOut() {
        signInClient.signOut()
    }
}