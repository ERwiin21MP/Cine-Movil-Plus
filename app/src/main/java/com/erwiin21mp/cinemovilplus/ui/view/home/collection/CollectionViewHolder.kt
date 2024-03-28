package com.erwiin21mp.cinemovilplus.ui.view.home.collection

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.erwiin21mp.cinemovilplus.R
import com.erwiin21mp.cinemovilplus.data.network.firebase.LogDataBaseManager
import com.erwiin21mp.cinemovilplus.databinding.ItemCollectionBinding
import com.erwiin21mp.cinemovilplus.domain.model.CollectionModel
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso

class CollectionViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val binding = ItemCollectionBinding.bind(view)

    fun render(item: CollectionModel, onItemSelected: (Int) -> Unit) {
        binding.apply {
            Picasso.get().load(item.verticalImageURL).error(R.drawable.no_load_image)
                .into(ivPosterVertical, object : Callback {
                    override fun onSuccess() {
                        ivPosterVertical.visibility = View.VISIBLE
                        ivPosterVerticalLoading.visibility = View.GONE
                    }

                    override fun onError(exception: Exception?) {
                        LogDataBaseManager().logErrorLoadImagePlatform(
                            exception,
                            item.name.orEmpty(),
                            item.verticalImageURL.orEmpty()
                        )
                    }

                })
            tvTitle.text = item.name
        }
        itemView.setOnClickListener { onItemSelected(item.id ?: 0) }
    }
}