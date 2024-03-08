package com.erwiin21mp.cinemovilplus.ui.view.home.platforms

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.erwiin21mp.cinemovilplus.R
import com.erwiin21mp.cinemovilplus.core.ext.toURLImage
import com.erwiin21mp.cinemovilplus.databinding.ItemPlatformBinding
import com.erwiin21mp.cinemovilplus.domain.model.ItemMXModel
import com.squareup.picasso.Picasso

class PlatformsViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val binding = ItemPlatformBinding.bind(view)
    fun render(item: ItemMXModel, onItemSelected: (String) -> Unit) {
        binding.apply {
            tvPlatform.text = item.providerName
            Picasso.get().load(item.logoPath!!.toURLImage()).error(R.drawable.no_image).into(ivPlatform)
        }
        itemView.setOnClickListener { onItemSelected(item.providerName!!) }
    }
}