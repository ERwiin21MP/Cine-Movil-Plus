package com.erwiin21mp.cinemovilplus.ui.view.home.viewpager2

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.erwiin21mp.cinemovilplus.databinding.ItemViewPagerBinding
import com.erwiin21mp.cinemovilplus.domain.model.ContentInitModel
import com.squareup.picasso.Picasso

class ViewPagerViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val binding = ItemViewPagerBinding.bind(view)

    fun render(item: ContentInitModel, onItemSelected: (Int) -> Unit) {
        Picasso.get().load(item.horizontalImageUrl).into(binding.imPoster)
        binding.tvTitle.text = item.title
        binding.root.setOnClickListener { onItemSelected(item.id) }
    }
}