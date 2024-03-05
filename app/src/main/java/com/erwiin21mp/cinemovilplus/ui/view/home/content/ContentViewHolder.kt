package com.erwiin21mp.cinemovilplus.ui.view.home.content

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.erwiin21mp.cinemovilplus.data.network.firebase.DataBaseManager
import com.erwiin21mp.cinemovilplus.databinding.ItemContentBinding
import com.erwiin21mp.cinemovilplus.domain.model.ContentInitModel
import com.erwiin21mp.cinemovilplus.ui.view.home.HomeViewModel.Companion.SERIE
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso

class ContentViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val binding = ItemContentBinding.bind(view)
    private val database = DataBaseManager()

    fun render(item: ContentInitModel, onItemSelected: (Int) -> Unit) {
        binding.apply {
            Picasso.get().load(item.verticalImageUrl).into(ivPosterVertical, object : Callback {
                override fun onSuccess() {
                    ivPosterVerticalLoading.visibility = View.GONE
                    ivPosterVertical.visibility = View.VISIBLE
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
            if (item.isCamQuality) ivCam.visibility = View.VISIBLE
            if (item.type == SERIE) ivShow.visibility = View.VISIBLE
        }
        itemView.setOnClickListener { onItemSelected(item.id) }
    }
}