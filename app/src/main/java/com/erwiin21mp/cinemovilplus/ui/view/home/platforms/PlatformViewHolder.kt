package com.erwiin21mp.cinemovilplus.ui.view.home.platforms

import android.view.View
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.erwiin21mp.cinemovilplus.databinding.ItemPlatformBinding
import com.erwiin21mp.cinemovilplus.domain.model.PlatformModel
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso

class PlatformViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val binding = ItemPlatformBinding.bind(view)

    fun render(item: PlatformModel, onItemSelectedListener: (String) -> Unit) {
        binding.apply {
            Picasso.get().load(item.url).into(ivLogoPlatform, object : Callback {
                override fun onSuccess() {
                    binding.pbItemPlatform.isVisible = false
                    itemView.setOnClickListener { onItemSelectedListener }
                }

                override fun onError(e: Exception?) {}
            })
        }
    }
}