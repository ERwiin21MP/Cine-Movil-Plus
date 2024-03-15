package com.erwiin21mp.cinemovilplus.ui.view.home.collection

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.erwiin21mp.cinemovilplus.R
import com.erwiin21mp.cinemovilplus.domain.model.CollectionModel

class CollectionAdapter(
    private var list: List<CollectionModel> = emptyList(),
    private val onItemSelected: (Int) -> Unit
) : RecyclerView.Adapter<CollectionViewHolder>() {
    fun updateList(listUpdated: List<CollectionModel>) {
        list = listUpdated
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = CollectionViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.item_collection, parent, false)
    )

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: CollectionViewHolder, position: Int) {
        holder.render(list[position], onItemSelected)
    }
}