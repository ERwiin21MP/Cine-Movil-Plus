package com.erwiin21mp.cinemovilplus.ui.view.home.content

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.erwiin21mp.cinemovilplus.data.network.DataBaseManager
import com.erwiin21mp.cinemovilplus.databinding.ItemContentBinding
import com.erwiin21mp.cinemovilplus.domain.model.ContentInitModel
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso

class ContentViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val binding = ItemContentBinding.bind(view)
    private val database = DataBaseManager()

    fun render(item: ContentInitModel, onItemSelected: (Int) -> Unit) {
        binding.apply {
            Picasso.get().load(item.verticalImageUrl).into(ivPosterVertical, object : Callback {
                override fun onSuccess() {
                    binding.ivPosterVerticalLoading.visibility = View.GONE
                    binding.ivPosterVertical.visibility = View.VISIBLE
                }

                override fun onError(exception: Exception?) {
                    database.logErrorLoadPosterImageContentVertical(
                        message = exception,
                        idContent = item.id,
                        verticalImageUlr = item.verticalImageUrl
                    )
                }
            })
            tvTitlePoster.text = item.title
        }
        itemView.setOnClickListener { onItemSelected(item.id) }
    }

}