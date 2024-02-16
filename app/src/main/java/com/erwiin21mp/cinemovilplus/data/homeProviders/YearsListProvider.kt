package com.erwiin21mp.cinemovilplus.data.homeProviders

import com.erwiin21mp.cinemovilplus.domain.model.ContentInitModel
import javax.inject.Inject

class YearsListProvider @Inject constructor() {
    fun getYearsList(content: List<ContentInitModel>): List<String> {
        val list = arrayListOf<Short>()
        val years = arrayListOf<String>()
        content.forEach { list.add(it.releaseDate.split("/")[2].toShort()) }
        list.sortDescending()
        list.forEach { years.add(it.toString()) }
        return years.distinct().toList()
    }
}