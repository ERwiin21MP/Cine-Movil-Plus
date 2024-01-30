package com.erwiin21mp.cinemovilplus

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
class Win {
    companion object {
        const val MIN_PASSWORD_LENGTH = 6
        const val MIN_USERNAME_LENGTH = 3
    }

    fun isValidEmail(email: String) =
        Patterns.EMAIL_ADDRESS.matcher(email).matches() || email.isEmpty()

    fun isValidPassword(password: String): Boolean =
        password.length >= MIN_PASSWORD_LENGTH || password.isEmpty()

    fun isValidUserName(userName: String): Boolean =
        userName.length >= MIN_USERNAME_LENGTH || userName.isEmpty()

    fun setError(view: EditText, message: String) {
        view.apply {
            error = message
            requestFocus()
            setBackgroundResource(R.drawable.bg_edit_text_error)
        }
        showSoftKeyboard(view)
    }

    fun showSoftKeyboard(view: View) {
        if (view.requestFocus()) {
            val imm =
                view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
        }
    }

    fun hideSoftKeyboard(view: View) {
        val imm =
            view.context.getSystemService(AppCompatActivity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    @SuppressLint("InflateParams")
    fun showAlertOfWaiting(context: Context, layout: Int): Dialog =
        AlertDialog.Builder(context, R.style.AlertWithOutBackground).apply {
            setView(LayoutInflater.from(context).inflate(layout, null))
            setCancelable(false)
            create()
        }.show()
}