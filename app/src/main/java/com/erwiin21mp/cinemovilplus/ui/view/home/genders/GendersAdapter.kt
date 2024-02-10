package com.erwiin21mp.cinemovilplus.ui.view.home.genders

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.erwiin21mp.cinemovilplus.R
import com.erwiin21mp.cinemovilplus.domain.model.GenderModel

class GendersAdapter(
    private var list: List<GenderModel>,
    private val onItemSelected: (String) -> Unit
) :
    RecyclerView.Adapter<GenderViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = GenderViewHolder(
        LayoutInflater.from(parent.context).inflate(
            R.layout.item_gender, null
        )
    )

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: GenderViewHolder, position: Int) {
        holder.render(list[position], onItemSelected)
    }

    fun updateList(listUpdated: List<GenderModel>) {
        list = listUpdated
        notifyDataSetChanged()
    }
}