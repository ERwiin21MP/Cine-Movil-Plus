package com.erwiin21mp.cinemovilplus.ui.view.home.content

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.erwiin21mp.cinemovilplus.R
import com.erwiin21mp.cinemovilplus.domain.model.ContentInitModel

class ContentAdapter(
    private var list: List<ContentInitModel> = emptyList(),
    private val onItemSelected: (Int) -> Unit
) : RecyclerView.Adapter<ContentViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ContentViewHolder(
        LayoutInflater.from(parent.context).inflate(
            R.layout.item_content, null
        )
    )

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: ContentViewHolder, position: Int) {
        holder.render(list[position], onItemSelected)
    }
}