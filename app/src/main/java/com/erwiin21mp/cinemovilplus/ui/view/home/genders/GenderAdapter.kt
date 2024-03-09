package com.erwiin21mp.cinemovilplus.ui.view.home.genders

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.erwiin21mp.cinemovilplus.R
import com.erwiin21mp.cinemovilplus.domain.model.GenderModel

class GenderAdapter(
    private var list: List<GenderModel> = emptyList(),
    private val onItemSelected: (Int) -> Unit
) : RecyclerView.Adapter<GenderViewHolder>() {

    fun updateList(listUpdated: List<GenderModel>) {
        list = listUpdated
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)= GenderViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_gender, parent, false))

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: GenderViewHolder, position: Int) {
        holder.render(list[position], onItemSelected)
    }
}