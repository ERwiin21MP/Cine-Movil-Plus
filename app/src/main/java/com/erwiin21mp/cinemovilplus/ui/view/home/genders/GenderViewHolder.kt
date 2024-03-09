package com.erwiin21mp.cinemovilplus.ui.view.home.genders

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.erwiin21mp.cinemovilplus.databinding.ItemGenderBinding
import com.erwiin21mp.cinemovilplus.domain.model.GenderModel

class GenderViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val binding = ItemGenderBinding.bind(view)

    fun render(item: GenderModel, onItemSelected: (Int) -> Unit) {
        binding.tvGender.text = item.gender
        itemView.setOnClickListener { onItemSelected(item.id) }
    }
}