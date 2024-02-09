package com.erwiin21mp.cinemovilplus.ui.view.home.platforms

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.erwiin21mp.cinemovilplus.R
import com.erwiin21mp.cinemovilplus.domain.model.PlatformModel

class PlatformAdapter(
    private var list: List<PlatformModel> = emptyList(),
    private var onItemSelectedListener: (String) -> Unit
) : RecyclerView.Adapter<PlatformViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = PlatformViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.item_platform, parent, false)
    )

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: PlatformViewHolder, position: Int) {
        holder.render(list[position], onItemSelectedListener)
    }

    fun updateList(listUpdated: List<PlatformModel>) {
        list = listUpdated
        notifyDataSetChanged()
    }
}