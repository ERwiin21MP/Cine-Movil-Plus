package com.erwiin21mp.cinemovilplus.core.extensions

import android.annotation.SuppressLint
import android.app.Activity
import android.util.Log
import android.view.LayoutInflater
import android.widget.TextView
import android.widget.Toast
import com.erwiin21mp.cinemovilplus.R
import java.text.SimpleDateFormat
import java.util.*

fun Any?.isNull() = this == null


fun Activity.logData(message: String) {
    Log.e("LOGDATA", message)
}

fun Activity.toast(message: String) {
    Toast(this).apply {
        val viewToast = LayoutInflater.from(this@toast).inflate(R.layout.view_toast, null)
        view = viewToast
        val label: TextView = viewToast.findViewById(R.id.toast)
        label.text = message
        duration = Toast.LENGTH_SHORT
        show()
    }
}

@SuppressLint("SimpleDateFormat")
fun Activity.getCurrentDateAndHour() = SimpleDateFormat("d MMM yyyy, EEE, HH:mm:ss z").format(Date()).toString()