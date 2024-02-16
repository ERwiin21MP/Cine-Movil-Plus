package com.erwiin21mp.cinemovilplus.data.homeProviders

import android.app.Application
import com.erwiin21mp.cinemovilplus.R
import com.erwiin21mp.cinemovilplus.domain.model.ContentInitModel
import com.erwiin21mp.cinemovilplus.domain.model.LabelContentModel
import javax.inject.Inject

class LabelsListProvider @Inject constructor(private val application: Application) {
    fun getListOfLabels(
        contentList: List<ContentInitModel>,
        listOfSagas: List<String>,
        listOfYears: List<String>
    ): List<LabelContentModel> {
        val list = mutableListOf<LabelContentModel>()
        list.add(
            LabelContentModel(
                titleList = application.getString(R.string.allContent),
                contentList = contentList
            )
        )
        listOfSagas.forEach {
            list.add(
                LabelContentModel(
                    titleList = it,
                    contentList = filterSaga(it, contentList)
                )
            )
        }
        listOfYears.forEach {
            list.add(
                LabelContentModel(
                    titleList = it,
                    contentList = contentList.filter { itFilter ->
                        it == itFilter.releaseDate.split("/")[2]
                    })
            )
        }
        return list
    }

    private fun filterSaga(
        gender: String,
        contentList: List<ContentInitModel>
    ): List<ContentInitModel> {
        val listOfContentReturn: ArrayList<ContentInitModel> = arrayListOf()
        contentList.forEach { content ->
            content.genres.forEach { genderList ->
                if (genderList.removeRange(0, 1) == gender) listOfContentReturn.add(content)
            }
        }
        return listOfContentReturn.distinct()
    }
}