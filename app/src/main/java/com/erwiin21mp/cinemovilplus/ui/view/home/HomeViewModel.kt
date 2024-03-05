package com.erwiin21mp.cinemovilplus.ui.view.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.erwiin21mp.cinemovilplus.data.model.Content
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor() : ViewModel() {

    private val db = FirebaseFirestore.getInstance()
    val listOfContent = MutableLiveData<List<Content>>(emptyList())

    companion object {
        const val TYPE = "Tipo"
        const val PREFIX_GENDER = "G"
        const val PREFIX_SAGA = "S"
        const val SYNOPSIS = "Sinopsis"
        const val GENRES = "Generos"
        const val DURATION = "Duracion"
        const val DIRECTOR = "Director"
        const val CLASIFICATION = "Clasificacion"
        const val VERTICAL_IMAGE_URL = "PosterVerticalURL"
        const val HORIZONTAL_IMAGE_URL = "PosterHorizontalURL"
        const val URL_TRAILER = "TrailerURL"
        const val URL_CONTENT = "URL"
        const val RATING = "Valoracion"
        const val PLATFORMS_LIST = "Plataformas"
        const val PRODUCERS_LIST = "Productoras"
        const val MOVIE = "Pelicula"
        const val SERIE = "Serie"
        const val COLLECTION_CONTENIDO = "Contenido"
        const val DISTRIBUCION = "Distribucion"
        const val RELEASE_DATE = "FechaDeEstreno"
        const val IS_CALIDAD_CAM = "CalidadCam"
        const val NAME = "Name"
        const val URL = "URL"

        const val CONTENT = "content"
        const val ID = "id"
        const val TITLE = "title"
        const val ID_TMDB = "id_tmdb"
        const val TYPE_ID = "type_id"
        const val CONTENT_URL = "content_url"
        const val UPLOAD_DATE = "upload_date"
        const val KEYWORDS = "keywords"
        const val IS_CAMERA_QUALITY = "is_camera_quality"
        const val IS_ENABLED = "is_enabled"
    }

    init {
        getContent()
    }

    private fun getContent() {
        val list = mutableListOf<Content>()
        db.collection(CONTENT).get().addOnSuccessListener { documents ->
            for (document in documents) {
                val data = document.data
                list.add(
                    Content(
                        id = data[ID].toString().toInt(),
                        idTmdb = data[ID_TMDB].toString().toInt(),
                        contentUrl = data[CONTENT_URL].toString(),
                        isCameraQuality = data[IS_CAMERA_QUALITY].toString().toInt() != 0,
                        isEnabled = data[IS_ENABLED].toString().toInt() != 0,
                        keywords = data[KEYWORDS].toString(),
                        typeID = data[TYPE_ID].toString().toInt(),
                        uploadDate = LocalDateTime.parse(
                            data[UPLOAD_DATE].toString(),
                            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                        )
                    )
                )
            }
            listOfContent.postValue(list)
        }
    }
}