package com.erwiin21mp.cinemovilplus.ui.view.home.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.erwiin21mp.cinemovilplus.domain.model.ContentInitModel
import com.erwiin21mp.cinemovilplus.domain.model.ContentInitProvider
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor() : ViewModel() {

    private val db = FirebaseFirestore.getInstance()
    val listContentInitModel = MutableLiveData<List<ContentInitModel>>(emptyList())

    init {
        val list = mutableListOf<ContentInitModel>()

        db.collection(ContentInitProvider.CONTENIDO).get().addOnSuccessListener { documents ->
            for (document in documents) {
                val data = document.data
                list.add(
                    ContentInitModel(
                        id = data[ContentInitProvider.ID].toString().toInt(),
                        title = data[ContentInitProvider.TITLE].toString(),
                        synopsis = data[ContentInitProvider.SYNOPSIS].toString(),
                        genres = data[ContentInitProvider.GENRES].toString().split(",").toList(),
                        duration = data[ContentInitProvider.DURATION].toString().toInt(),
                        releaseDate = data[ContentInitProvider.RELEASE_DATE].toString(),
                        verticalImageUrl = data[ContentInitProvider.VERTICAL_IMAGE_URL].toString(),
                        horizontalImageUrl = data[ContentInitProvider.HORIZONTAL_IMAGE_URL].toString(),
                        rating = data[ContentInitProvider.RATING].toString().toInt(),
                        platformsList = data[ContentInitProvider.PLATFORMS_LIST].toString()
                            .split(",").toList(),
                        uploadDate = data[ContentInitProvider.UPLOAD_DATE].toString().toLong(),
                        producersList = data[ContentInitProvider.PRODUCERS_LIST].toString()
                            .split(",").toList(),
                        type = data[ContentInitProvider.TYPE].toString(),
                        keywords = data[ContentInitProvider.KEYWORDS].toString(),
                        isCamQuality = data[ContentInitProvider.CALIDAD_CAM].toString().toBoolean()
                    )
                )
                listContentInitModel.postValue(list)
            }
        }
    }
}