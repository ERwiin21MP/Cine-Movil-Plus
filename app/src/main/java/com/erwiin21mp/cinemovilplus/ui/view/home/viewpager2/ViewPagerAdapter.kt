package com.erwiin21mp.cinemovilplus.ui.view.home.viewpager2

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.erwiin21mp.cinemovilplus.R
import com.erwiin21mp.cinemovilplus.domain.model.ContentInitModel

class ViewPagerAdapter(
    private val list: List<ContentInitModel>,
    private val onItemSelected: (Int) -> Unit
) :
    RecyclerView.Adapter<ViewPagerViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewPagerViewHolder =
        ViewPagerViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_view_pager, parent, false
            )
        )

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: ViewPagerViewHolder, position: Int) {
        holder.render(list[position], onItemSelected)
    }
}