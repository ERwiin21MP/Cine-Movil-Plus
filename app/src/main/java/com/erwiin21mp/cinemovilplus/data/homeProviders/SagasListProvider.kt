package com.erwiin21mp.cinemovilplus.data.homeProviders

import com.erwiin21mp.cinemovilplus.domain.model.ContentInitModel
import com.erwiin21mp.cinemovilplus.ui.view.home.viewmodel.HomeViewModel
import javax.inject.Inject

class SagasListProvider @Inject constructor() {
    fun getListOfSagas(content: List<ContentInitModel>): List<String> {
        val list = arrayListOf<String>()
        content.forEach { contentModel ->
            contentModel.genres.forEach { gender ->
                if (gender[0].toString() == HomeViewModel.PREFIX_SAGA)
                    list.add(gender.removeRange(0, 1))
            }
        }
        return list.distinct().toList().sorted()
    }
}