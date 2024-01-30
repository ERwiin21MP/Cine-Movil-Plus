package com.erwiin21mp.cinemovilplus.core

import android.app.Activity
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.widget.TextView
import android.widget.Toast
import com.erwiin21mp.cinemovilplus.R

fun Any?.isNull() = this == null

fun Activity.log(message: String) {
    Log.e("LOG-DATA", message)
}

fun Activity.toast(message: String) {
        Toast(this).apply {
            val viewToast = LayoutInflater.from(this@toast).inflate(R.layout.view_toast, null)
            view = viewToast
            val label: TextView = viewToast.findViewById(R.id.toast)
            label.text = message
            duration = Toast.LENGTH_LONG
            show()
        }
}