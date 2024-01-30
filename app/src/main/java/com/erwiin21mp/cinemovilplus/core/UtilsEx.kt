package com.erwiin21mp.cinemovilplus.core

import android.app.Activity
import android.util.Log
import android.widget.Toast

fun Any?.isNull() = this == null

fun Activity.log(message: String) {
    Log.e("LOGDATA", message)
}

fun Activity.toast(text: String, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, text, duration).show()
}