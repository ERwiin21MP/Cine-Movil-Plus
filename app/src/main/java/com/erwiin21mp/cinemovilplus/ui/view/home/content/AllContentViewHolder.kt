package com.erwiin21mp.cinemovilplus.ui.view.home.content

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.erwiin21mp.cinemovilplus.R
import com.erwiin21mp.cinemovilplus.core.ext.toURLImage
import com.erwiin21mp.cinemovilplus.data.network.firebase.DataBaseManager
import com.erwiin21mp.cinemovilplus.databinding.ItemContentBinding
import com.erwiin21mp.cinemovilplus.domain.model.ContentHomeModel
import com.erwiin21mp.cinemovilplus.ui.view.home.HomeViewModel.Companion.SERIE
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso

class AllContentViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val binding = ItemContentBinding.bind(view)

    fun render(item: ContentHomeModel, onItemSelected: (String) -> Unit) {
        binding.apply {
            Picasso.get().load(item.verticalImageURL!!.toURLImage()).error(R.drawable.no_image)
                .into(ivPosterVertical, object : Callback {
                    override fun onSuccess() {
                        ivPosterVerticalLoading.visibility = View.GONE
                        ivPosterVertical.visibility = View.VISIBLE
                    }

                    override fun onError(exception: Exception?) {
                        DataBaseManager().logErrorLoadPosterImageContentVertical(
                            message = exception!!,
                            idContent = item.id.orEmpty(),
                            verticalImageUlr = item.verticalImageURL.orEmpty()
                        )
                    }
                })
            tvTitle.text = item.title
            if (item.isCameraQuality == true) ivCameraQuality.visibility = View.VISIBLE
            else ivCameraQuality.visibility = View.GONE
            if (item.type == SERIE) ivSerie.visibility = View.VISIBLE
            else ivSerie.visibility = View.GONE
        }
        itemView.setOnClickListener { onItemSelected(item.id!!) }
    }
}