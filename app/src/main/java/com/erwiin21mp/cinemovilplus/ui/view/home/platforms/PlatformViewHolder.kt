package com.erwiin21mp.cinemovilplus.ui.view.home.platforms

import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.recyclerview.widget.RecyclerView
import com.erwiin21mp.cinemovilplus.data.network.DataBaseManager
import com.erwiin21mp.cinemovilplus.databinding.ItemPlatformBinding
import com.erwiin21mp.cinemovilplus.domain.model.PlatformModel
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso

class PlatformViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val binding = ItemPlatformBinding.bind(view)
    private val database = DataBaseManager()

    fun render(item: PlatformModel, onItemSelectedListener: (String) -> Unit) {
        binding.apply {
            Picasso.get().load(item.url).into(ivLogoPlatform, object : Callback {
                override fun onSuccess() {
                    sflLogoPlatformLoading.visibility = GONE
                    ivLogoPlatform.visibility = VISIBLE
                    itemView.setOnClickListener { onItemSelectedListener(item.name) }
                }

                override fun onError(exception: Exception?) {
                    database.logErrorLoadImagePlatform(
                        message = exception,
                        name = item.name,
                        url = item.url
                    )
                }
            })
        }
    }
}