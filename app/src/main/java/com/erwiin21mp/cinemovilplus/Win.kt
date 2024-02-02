package com.erwiin21mp.cinemovilplus

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.erwiin21mp.cinemovilplus.data.network.AuthManager
import com.erwiin21mp.cinemovilplus.data.network.DataBaseManager
import com.erwiin21mp.cinemovilplus.databinding.AlertMessageBinding
import com.erwiin21mp.cinemovilplus.databinding.AlertWaitingBinding

class Win {

    private val database = DataBaseManager()
    private val auth = AuthManager()

    companion object {
        const val MIN_PASSWORD_LENGTH = 6
        const val MIN_USERNAME_LENGTH = 3
        const val BUTTON_ALERT_VALE = "AlertVale"
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
    fun getAndShowAlertOfWaiting(context: Context, title: String): Dialog =
        AlertDialog.Builder(context, R.style.AlertWithOutBackground).apply {
            val view = LayoutInflater.from(context).inflate(R.layout.alert_waiting, null)
            val binding = AlertWaitingBinding.bind(view)
            binding.apply { tvAlertTitle.text = title }
            setView(view)
            setCancelable(false)
            create()
        }.show()

    @SuppressLint("InflateParams")
    fun showAlertOfMessage(
        context: Context,
        title: String,
        message: String,
        isVisibleContact: Boolean
    ) {
        val view = LayoutInflater.from(context).inflate(R.layout.alert_message, null)
        val binding = AlertMessageBinding.bind(view)

        val builder = AlertDialog.Builder(context, R.style.AlertWithOutBackground).apply {
            setView(view)
            create()
        }

        val dialog = builder.show()
        binding.apply {
            tvAlertTitle.text = title
            tvAlertMessage.text = message
            btnVale.setOnClickListener { dialog.dismiss() }

            tvAlertContact.setOnClickListener {
                database.logButtonClicked(BUTTON_ALERT_VALE, auth.getCurrentUser())
                val intent = Intent(Intent.ACTION_SENDTO).apply {
                    data = Uri.parse(
                        "mailto:${context.getString(R.string.email)}?subject=${
                            Uri.encode("${context.getString(R.string.app_name)} - Error de Inicio de Sesión")
                        }"
                    )
                }
                context.startActivity(intent)
                dialog.dismiss()
            }

            if (!isVisibleContact) tvAlertContact.visibility = View.GONE
        }
    }
}