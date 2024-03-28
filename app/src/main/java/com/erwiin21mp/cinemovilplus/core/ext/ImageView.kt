package com.erwiin21mp.cinemovilplus.core.ext

import android.widget.ImageView
import androidx.annotation.DrawableRes
import com.erwiin21mp.cinemovilplus.R
import com.squareup.picasso.Picasso

fun ImageView.loadImage(url: String, @DrawableRes errorImage: Int = R.drawable.no_load_image) {
    Picasso.get().load(url).error(errorImage).into(this, )
}