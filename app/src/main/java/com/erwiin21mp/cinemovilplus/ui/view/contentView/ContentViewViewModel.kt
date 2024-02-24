package com.erwiin21mp.cinemovilplus.ui.view.contentView

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.erwiin21mp.cinemovilplus.domain.model.ContentModel
import com.erwiin21mp.cinemovilplus.ui.view.home.HomeViewModel.Companion.CLASIFICATION
import com.erwiin21mp.cinemovilplus.ui.view.home.HomeViewModel.Companion.COLLECTION_CONTENIDO
import com.erwiin21mp.cinemovilplus.ui.view.home.HomeViewModel.Companion.DIRECTOR
import com.erwiin21mp.cinemovilplus.ui.view.home.HomeViewModel.Companion.DISTRIBUCION
import com.erwiin21mp.cinemovilplus.ui.view.home.HomeViewModel.Companion.DURATION
import com.erwiin21mp.cinemovilplus.ui.view.home.HomeViewModel.Companion.GENRES
import com.erwiin21mp.cinemovilplus.ui.view.home.HomeViewModel.Companion.HORIZONTAL_IMAGE_URL
import com.erwiin21mp.cinemovilplus.ui.view.home.HomeViewModel.Companion.ID
import com.erwiin21mp.cinemovilplus.ui.view.home.HomeViewModel.Companion.IS_CALIDAD_CAM
import com.erwiin21mp.cinemovilplus.ui.view.home.HomeViewModel.Companion.KEYWORDS
import com.erwiin21mp.cinemovilplus.ui.view.home.HomeViewModel.Companion.PLATFORMS_LIST
import com.erwiin21mp.cinemovilplus.ui.view.home.HomeViewModel.Companion.PRODUCERS_LIST
import com.erwiin21mp.cinemovilplus.ui.view.home.HomeViewModel.Companion.RATING
import com.erwiin21mp.cinemovilplus.ui.view.home.HomeViewModel.Companion.RELEASE_DATE
import com.erwiin21mp.cinemovilplus.ui.view.home.HomeViewModel.Companion.SYNOPSIS
import com.erwiin21mp.cinemovilplus.ui.view.home.HomeViewModel.Companion.TITLE
import com.erwiin21mp.cinemovilplus.ui.view.home.HomeViewModel.Companion.TYPE
import com.erwiin21mp.cinemovilplus.ui.view.home.HomeViewModel.Companion.UPLOAD_DATE
import com.erwiin21mp.cinemovilplus.ui.view.home.HomeViewModel.Companion.URL_CONTENT
import com.erwiin21mp.cinemovilplus.ui.view.home.HomeViewModel.Companion.URL_TRAILER
import com.erwiin21mp.cinemovilplus.ui.view.home.HomeViewModel.Companion.VERTICAL_IMAGE_URL
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ContentViewViewModel @Inject constructor() : ViewModel() {

    private val db = FirebaseFirestore.getInstance()
    val itemContent = MutableLiveData<ContentModel>()

    fun getItemContent(id: String) {
        val item = ContentModel()
        db.collection(COLLECTION_CONTENIDO).document(id).get().addOnCompleteListener { task ->
            val data = task.result
            item.id = data[ID].toString().toInt()
            item.title = data[TITLE].toString()
            item.synopsis = data[SYNOPSIS].toString()
            item.genres =
                data[GENRES].toString().replace("[", "").replace("]", "").plus(" ").split(", ")
                    .map { it.trim() }
            item.duration = data[DURATION].toString().toInt()
            item.director = data[DIRECTOR].toString()
            item.releaseDate = data[RELEASE_DATE].toString()
            item.classification = data[CLASIFICATION].toString()
            item.verticalImageUrl = data[VERTICAL_IMAGE_URL].toString()
            item.horizontalImageUrl = data[HORIZONTAL_IMAGE_URL].toString()
            item.trailerUrl = data[URL_TRAILER].toString()
            item.contentUrl = data[URL_CONTENT].toString()
            item.rating = data[RATING].toString().toInt()
            item.platformsList =
                data[PLATFORMS_LIST].toString().replace("[", "").replace("]", "").plus(" ")
                    .split(", ").map { it.trim() }
            item.uploadDate = data[UPLOAD_DATE].toString().toLong()
            item.producersList =
                data[PRODUCERS_LIST].toString().replace("[", "").replace("]", "").plus(" ")
                    .split(", ").map { it.trim() }
            item.type = data[TYPE].toString()
            item.distribution = data[DISTRIBUCION].toString()
            item.keywords = data[KEYWORDS].toString()
            item.isCamQuality = data[IS_CALIDAD_CAM].toString().toBoolean()
            itemContent.postValue(item)
        }
    }
}