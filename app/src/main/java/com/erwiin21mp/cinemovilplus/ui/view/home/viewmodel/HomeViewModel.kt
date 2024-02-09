package com.erwiin21mp.cinemovilplus.ui.view.home.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.erwiin21mp.cinemovilplus.domain.model.ContentInitModel
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor() : ViewModel() {

    private val db = FirebaseFirestore.getInstance()
    val listOfContent = MutableLiveData<List<ContentInitModel>>(emptyList())
    val listOfPlatforms = MutableLiveData<List<>>

    private companion object {
        const val COLLECTION_PLATFORM_IMAGE_URLS = "PlatformImageURLs"
        const val ID = "Id"
        const val TYPE = "Tipo"
        const val GENERO_PREFIJO = "G"
        const val SAGA_PREFIJO = "S"
        const val TITLE = "Titulo"
        const val SYNOPSIS = "Sinopsis"
        const val GENRES = "Generos"
        const val DURATION = "Duracion"
        const val DIRECTOR = "Director"
        const val CLASIFICACION = "Clasificacion"
        const val VERTICAL_IMAGE_URL = "PosterVerticalURL"
        const val HORIZONTAL_IMAGE_URL = "PosterHorizontalURL"
        const val URL_TRAILER = "TrailerURL"
        const val URL_CONTENT = "URL"
        const val RATING = "Valoracion"
        const val PLATFORMS_LIST = "Plataformas"
        const val UPLOAD_DATE = "FechaDeSubida"
        const val PRODUCERS_LIST = "Productoras"
        const val PELICULA = "Pelicula"
        const val SERIE = "Serie"
        const val COLLECTION_CONTENIDO = "Contenido"
        const val DISTRIBUCION = "Distribucion"
        const val KEYWORDS = "PalabrasClave"
        const val RELEASE_DATE = "FechaDeEstreno"
        const val CALIDAD_CAM = "CalidadCam"
    }

    init {
        getInitContent()
    }

    private fun getInitContent() {
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

    private fun getPlatforms() {
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