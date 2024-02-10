package com.erwiin21mp.cinemovilplus.ui.view.home.genders

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.erwiin21mp.cinemovilplus.databinding.ItemGenderBinding
import com.erwiin21mp.cinemovilplus.domain.model.GenderModel
import com.squareup.picasso.Picasso

class GenderViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val binding = ItemGenderBinding.bind(view)
    fun render(item: GenderModel, onItemSelected: (String) -> Unit) {
        binding.apply {
            tvGender.text = item.gender
            Picasso.get().load(item.urlPicture).into(this.ivBgGender)
        }
        itemView.setOnClickListener { onItemSelected(item.gender) }
    }
}