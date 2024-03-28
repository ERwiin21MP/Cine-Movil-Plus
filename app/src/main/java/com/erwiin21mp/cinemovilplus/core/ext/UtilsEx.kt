package com.erwiin21mp.cinemovilplus.core.ext

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.RecyclerView
import com.erwiin21mp.cinemovilplus.R
import java.text.SimpleDateFormat
import java.util.*

fun Any?.isNull() = this == null

fun Any?.isNotNull() = this != null

fun Any.logData(message: Any?, tag: String = "") {
    Log.e("Erwin $tag", message.toString())
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

fun Fragment.toast(message: String) {
    Toast(context).apply {
        val viewToast = LayoutInflater.from(context).inflate(R.layout.view_toast, null)
        view = viewToast
        val label: TextView = viewToast.findViewById(R.id.toast)
        label.text = message
        duration = Toast.LENGTH_SHORT
        show()
    }
}

fun RecyclerView.ViewHolder.toast(context: Context, message: String) {
    Toast(context).apply {
        val viewToast = LayoutInflater.from(context).inflate(R.layout.view_toast, null)
        view = viewToast
        val label: TextView = viewToast.findViewById(R.id.toast)
        label.text = message
        duration = Toast.LENGTH_SHORT
        show()
    }
}

@SuppressLint("SimpleDateFormat")
fun Activity.getCurrentDateAndHour() =
    SimpleDateFormat("d MMM yyyy, EEE, HH:mm:ss z").format(Date()).toString()

fun Activity.printList(list: List<Any>, tag: String = "") {
    list.forEach { Log.e("Erwin $tag", it.toString()) }
}

fun Fragment.logList(list: List<Any>, tag: String = "") {
    if (list.isNotEmpty()) list.forEach { logData(it.toString(), tag) }
    else logData("Empty list")
}

fun ViewModel.logList(list: List<Any>, tag: String = "") {
    if (list.isNotEmpty()) list.forEach { logData(it.toString(), tag) }
    else logData("Empty list")
}