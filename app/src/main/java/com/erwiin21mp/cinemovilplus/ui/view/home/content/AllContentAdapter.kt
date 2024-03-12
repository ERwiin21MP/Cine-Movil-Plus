package com.erwiin21mp.cinemovilplus.ui.view.home.content

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.erwiin21mp.cinemovilplus.R
import com.erwiin21mp.cinemovilplus.domain.model.ContentHomeModel

class AllContentAdapter(
    private var list: List<ContentHomeModel> = emptyList(),
    private val onItemSelected: (String) -> Unit
) : RecyclerView.Adapter<AllContentViewHolder>() {

    fun updateList(listUpdated: List<ContentHomeModel>) {
        list = listUpdated
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = AllContentViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.item_content, parent, false)
    )

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: AllContentViewHolder, position: Int) {
        holder.render(list[position], onItemSelected)
    }
}