package com.erwiin21mp.cinemovilplus.ui.view.home.content

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.erwiin21mp.cinemovilplus.databinding.ItemContentBinding
import com.erwiin21mp.cinemovilplus.domain.model.ContentInitModel
import com.squareup.picasso.Picasso

class ContentViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    val binding = ItemContentBinding.bind(view)

    fun render(item: ContentInitModel, onItemSelected: (Int) -> Unit) {
        binding.apply {
            Picasso.get().load(item.verticalImageUrl).into(ivPosterVertical)
            tvTitlePoster.text = item.title
        }
        itemView.setOnClickListener { onItemSelected(item.id) }
    }

}