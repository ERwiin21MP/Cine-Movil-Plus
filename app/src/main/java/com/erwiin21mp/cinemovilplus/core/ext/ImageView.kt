package com.erwiin21mp.cinemovilplus.core.ext

import android.widget.ImageView
import androidx.annotation.DrawableRes
import com.erwiin21mp.cinemovilplus.R
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso

fun ImageView.loadImage(
    url: String,
    @DrawableRes errorImage: Int = R.drawable.no_load_image,
    onSuccess: () -> Unit = {},
    onErrorL: (String) -> Unit = {}
) {
    Picasso.get().load(url).error(errorImage).into(this, object : Callback {
        override fun onSuccess() {
            onSuccess()
        }

        override fun onError(e: Exception?) {
            onErrorL(e?.message ?: "Error desconocido")
        }
    })
}