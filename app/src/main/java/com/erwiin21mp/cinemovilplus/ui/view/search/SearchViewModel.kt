package com.erwiin21mp.cinemovilplus.ui.view.search

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.erwiin21mp.cinemovilplus.domain.model.ContentInitModel
import com.erwiin21mp.cinemovilplus.ui.view.home.HomeViewModel.Companion.CALIDAD_CAM
import com.erwiin21mp.cinemovilplus.ui.view.home.HomeViewModel.Companion.COLLECTION_CONTENIDO
import com.erwiin21mp.cinemovilplus.ui.view.home.HomeViewModel.Companion.DURATION
import com.erwiin21mp.cinemovilplus.ui.view.home.HomeViewModel.Companion.GENRES
import com.erwiin21mp.cinemovilplus.ui.view.home.HomeViewModel.Companion.HORIZONTAL_IMAGE_URL
import com.erwiin21mp.cinemovilplus.ui.view.home.HomeViewModel.Companion.ID
import com.erwiin21mp.cinemovilplus.ui.view.home.HomeViewModel.Companion.KEYWORDS
import com.erwiin21mp.cinemovilplus.ui.view.home.HomeViewModel.Companion.PLATFORMS_LIST
import com.erwiin21mp.cinemovilplus.ui.view.home.HomeViewModel.Companion.PRODUCERS_LIST
import com.erwiin21mp.cinemovilplus.ui.view.home.HomeViewModel.Companion.RATING
import com.erwiin21mp.cinemovilplus.ui.view.home.HomeViewModel.Companion.RELEASE_DATE
import com.erwiin21mp.cinemovilplus.ui.view.home.HomeViewModel.Companion.SYNOPSIS
import com.erwiin21mp.cinemovilplus.ui.view.home.HomeViewModel.Companion.TITLE
import com.erwiin21mp.cinemovilplus.ui.view.home.HomeViewModel.Companion.TYPE
import com.erwiin21mp.cinemovilplus.ui.view.home.HomeViewModel.Companion.UPLOAD_DATE
import com.erwiin21mp.cinemovilplus.ui.view.home.HomeViewModel.Companion.VERTICAL_IMAGE_URL
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor() : ViewModel() {

    private val db = FirebaseFirestore.getInstance()
    val listOfContent = MutableLiveData<List<ContentInitModel>>(emptyList())

    init {
        getContent()
    }

    private fun getContent() {
        val list = mutableListOf<ContentInitModel>()
        db.collection(COLLECTION_CONTENIDO).get().addOnSuccessListener { documents ->
            for (document in documents) {
                val data = document.data
                list.add(
                    ContentInitModel(
                        id = data[ID].toString().toInt(),
                        title = data[TITLE].toString(),
                        synopsis = data[SYNOPSIS].toString(),
                        genres = data[GENRES].toString().replace("[", "")
                            .replace("]", "").plus(" ").split(", ").map { it.trim() },
                        duration = data[DURATION].toString().toInt(),
                        releaseDate = data[RELEASE_DATE].toString(),
                        verticalImageUrl = data[VERTICAL_IMAGE_URL].toString(),
                        horizontalImageUrl = data[HORIZONTAL_IMAGE_URL].toString(),
                        rating = data[RATING].toString().toInt(),
                        platformsList = data[PLATFORMS_LIST].toString()
                            .replace("[", "").replace("]", "").plus(" ").split(", ")
                            .map { it.trim() },
                        uploadDate = data[UPLOAD_DATE].toString().toLong(),
                        producersList = data[PRODUCERS_LIST].toString()
                            .replace("[", "").replace("]", "").plus(" ").split(", ")
                            .map { it.trim() },
                        type = data[TYPE].toString(),
                        keywords = data[KEYWORDS].toString(),
                        isCamQuality = data[CALIDAD_CAM].toString().toBoolean()
                    )
                )
                listOfContent.postValue(list)
            }
        }
    }
}