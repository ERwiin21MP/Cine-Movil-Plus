package com.erwiin21mp.cinemovilplus.ui.view.home.genders

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.erwiin21mp.cinemovilplus.databinding.ItemGenderBinding
import com.erwiin21mp.cinemovilplus.domain.model.GenderModel
import com.squareup.picasso.Picasso
import jp.wasabeef.picasso.transformations.CropCircleTransformation

class GenderViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val binding = ItemGenderBinding.bind(view)

    fun render(item: GenderModel, onItemSelected: (Int) -> Unit) {
        binding.apply {
            tvGender.text = item.gender
            Picasso.get().load(item.imageURL).transform(CropCircleTransformation()).into(ivGender)
        }
        itemView.setOnClickListener { onItemSelected(item.id) }
    }
}