package com.erwiin21mp.cinemovilplus.ui.view.home.platforms

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.erwiin21mp.cinemovilplus.R
import com.erwiin21mp.cinemovilplus.domain.model.FlatrateModel

class PlatformsAdapter(
    private var list: List<FlatrateModel> = emptyList(),
    private val onItemSelected: (String) -> Unit
) : RecyclerView.Adapter<PlatformsViewHolder>() {

    fun updateList(listUpdated: List<FlatrateModel>) {
        list = listUpdated
        notifyDataSetChanged()
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = PlatformsViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.item_platform, parent, false)
    )

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: PlatformsViewHolder, position: Int) {
        holder.render(list[position], onItemSelected)
    }
}