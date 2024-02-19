package com.erwiin21mp.cinemovilplus.data.homeProviders

import com.erwiin21mp.cinemovilplus.domain.model.ContentInitModel
import com.erwiin21mp.cinemovilplus.domain.model.GenderModel
import com.erwiin21mp.cinemovilplus.ui.view.home.HomeViewModel.Companion.PREFIX_SAGA
import javax.inject.Inject

class GendersListProvider @Inject constructor() {
    fun getGendersList(listOfContent: List<ContentInitModel>): MutableList<GenderModel> {
        val listOfGenders: MutableList<GenderModel> = mutableListOf()
        listOfContent.forEach { content ->
            content.genres.forEach { gender ->
                if (gender[0].toString() != PREFIX_SAGA) listOfGenders.add(
                    GenderModel(
                        gender = gender.removeRange(0, 1),
                        urlPicture = content.horizontalImageUrl
                    )
                )
            }
        }
        listOfGenders.sortBy { it.gender }
        return listOfGenders.distinctBy { it.gender }.toMutableList()
    }
}