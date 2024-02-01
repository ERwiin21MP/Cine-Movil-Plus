package com.erwiin21mp.cinemovilplus.core

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AlertDialog
import com.erwiin21mp.cinemovilplus.R
import com.erwiin21mp.cinemovilplus.databinding.AlertWaitingBinding

fun View.dismissKeyboard(completed: () -> Unit = {}) {
    val inputMethodManager =
        context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    val wasOpened = inputMethodManager.hideSoftInputFromWindow(windowToken, 0)
    if (!wasOpened) completed()
}

fun Activity.getAndShowAlertOfWaiting(title: String, message: String): Dialog =
    AlertDialog.Builder(this, R.style.AlertWithOutBackground).apply {
        val view = LayoutInflater.from(context).inflate(R.layout.alert_waiting, null)
        val binding = AlertWaitingBinding.bind(view)
        binding.apply {
            tvAlertTitle.text = title
            tvAlertMessage.text = message
        }
        setView(view)
        setCancelable(false)
        create()
    }.show()