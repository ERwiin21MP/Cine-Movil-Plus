package com.erwiin21mp.cinemovilplus.ui.view.home.content

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.erwiin21mp.cinemovilplus.R
import com.erwiin21mp.cinemovilplus.domain.model.ContentModel

class ContentAdapter(
    private var list: List<ContentModel> = emptyList(),
    private val onItemSelected: (String) -> Unit
) : RecyclerView.Adapter<ContentViewHolder>() {

    fun updateList(listUpdated: List<ContentModel>) {
        list = listUpdated
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ContentViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.item_content, parent, false)
    )

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: ContentViewHolder, position: Int) {
        holder.render(list[position], onItemSelected)
    }
}