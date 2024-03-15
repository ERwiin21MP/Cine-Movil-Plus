package com.erwiin21mp.cinemovilplus.ui.view.home.platforms

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.erwiin21mp.cinemovilplus.R
import com.erwiin21mp.cinemovilplus.core.ext.toast
import com.erwiin21mp.cinemovilplus.data.network.firebase.DataBaseManager
import com.erwiin21mp.cinemovilplus.databinding.ItemPlatformBinding
import com.erwiin21mp.cinemovilplus.domain.model.FlatrateModel
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso

class PlatformsViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val binding = ItemPlatformBinding.bind(view)
    fun render(item: FlatrateModel, onItemSelected: (String) -> Unit) {
        binding.apply {
            Picasso.get().load(item.imageURL).error(R.drawable.no_image)
                .into(ivPlatform, object : Callback {
                    override fun onSuccess() {
                        containerIvPlatform.visibility = View.GONE
                        ivPlatform.visibility = View.VISIBLE
                        itemView.apply {
                            setOnClickListener { onItemSelected(item.name!!) }
                            setOnLongClickListener {
                                toast(itemView.context, item.name!!)
                                true
                            }
                        }
                    }

                    override fun onError(exception: Exception?) {
                        DataBaseManager().logErrorLoadImagePlatform(
                            message = exception,
                            name = item.name.orEmpty(),
                            url = item.imageURL.orEmpty()
                        )
                    }
                })
        }
    }
}