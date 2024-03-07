package com.erwiin21mp.cinemovilplus.ui.view.home.viewPager2

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.erwiin21mp.cinemovilplus.R
import com.erwiin21mp.cinemovilplus.domain.model.ContentHomeModel

class ViewPagerAdapter(
    private var list: List<ContentHomeModel> = emptyList(),
    private val onItemSelected: (String) -> Unit
) : RecyclerView.Adapter<ViewPagerHolder>() {

    fun updateList(listUpdated: List<ContentHomeModel>) {
        list = listUpdated
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewPagerHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.item_view_pager, parent, false)
    )

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: ViewPagerHolder, position: Int) {
        holder.render(list[position], onItemSelected)
    }
}