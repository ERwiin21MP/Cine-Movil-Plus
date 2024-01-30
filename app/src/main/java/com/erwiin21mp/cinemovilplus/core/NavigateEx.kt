package com.erwiin21mp.cinemovilplus.core

import android.app.Activity
import android.content.Intent
import com.erwiin21mp.cinemovilplus.ui.view.IndexActivity

fun Activity.navigateToIndex() {
    this.startActivity(Intent(this, IndexActivity::class.java))
}