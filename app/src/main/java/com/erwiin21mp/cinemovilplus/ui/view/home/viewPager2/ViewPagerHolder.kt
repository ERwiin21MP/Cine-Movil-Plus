package com.erwiin21mp.cinemovilplus.ui.view.home.viewPager2

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.erwiin21mp.cinemovilplus.R
import com.erwiin21mp.cinemovilplus.core.ext.toImageURL
import com.erwiin21mp.cinemovilplus.databinding.ItemViewPagerBinding
import com.erwiin21mp.cinemovilplus.domain.model.ContentHomeModel
import com.squareup.picasso.Picasso

class ViewPagerHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val binding = ItemViewPagerBinding.bind(view)
    fun render(item: ContentHomeModel?, onItemSelected: (String) -> Unit) {
        binding.apply {
            Picasso.get().load(item?.horizontalImageURL?.toImageURL()).error(R.drawable.no_image)
                .into(ivPoster)
            containerIVPosterLoading.visibility = View.GONE
            ivPoster.visibility = View.VISIBLE
            tvTitle.text = item?.title
        }
        itemView.setOnClickListener { onItemSelected(item?.id ?: "0") }
    }
}