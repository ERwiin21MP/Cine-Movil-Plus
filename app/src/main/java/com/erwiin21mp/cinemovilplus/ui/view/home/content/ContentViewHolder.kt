package com.erwiin21mp.cinemovilplus.ui.view.home.content

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.erwiin21mp.cinemovilplus.R
import com.erwiin21mp.cinemovilplus.data.network.firebase.DataBaseManager
import com.erwiin21mp.cinemovilplus.databinding.ItemContentBinding
import com.erwiin21mp.cinemovilplus.domain.model.ContentModel
import com.erwiin21mp.cinemovilplus.domain.model.Type.Movie
import com.erwiin21mp.cinemovilplus.domain.model.Type.Serie
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso

class ContentViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val binding = ItemContentBinding.bind(view)

    fun render(item: ContentModel, onItemSelected: (String) -> Unit) {
        binding.apply {
            Picasso.get().load(item.verticalImageURL).error(R.drawable.no_image)
                .into(ivPosterVertical, object : Callback {
                    override fun onSuccess() {
                        ivPosterVerticalLoading.visibility = View.GONE
                        ivPosterVertical.visibility = View.VISIBLE
                    }

                    override fun onError(exception: Exception?) {
                        DataBaseManager().logErrorLoadPosterImageContentVertical(
                            message = exception!!,
                            idContent = item.id.toString(),
                            verticalImageUlr = item.verticalImageURL.orEmpty()
                        )
                    }
                })
            tvTitle.text = item.title
            when (item.isCameraQuality) {
                true -> ivCameraQuality.visibility = View.VISIBLE
                false, null -> ivCameraQuality.visibility = View.GONE
            }
            when (item.type) {
                Serie -> ivSerie.visibility = View.VISIBLE
                Movie, null -> ivSerie.visibility = View.GONE
            }
        }
        itemView.setOnClickListener { onItemSelected(item.id.toString()) }
    }
}