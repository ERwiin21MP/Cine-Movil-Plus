package com.erwiin21mp.cinemovilplus.domain.model

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject

class ContentInitProvider @Inject constructor() {

    companion object {
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
        const val CONTENIDO = "Contenido"
        const val DISTRIBUCION = "Distribucion"
        const val KEYWORDS = "PalabrasClave"
        const val RELEASE_DATE = "FechaDeEstreno"
        const val CALIDAD_CAM = "CalidadCam"


//        fun getAllContentInit(): List<ContentInitModel> {
//
//        }
    }

    private val db = FirebaseFirestore.getInstance()
    fun getContentInitModel(): List<ContentInitModel> {
        val list = mutableListOf<ContentInitModel>()

        db.collection(CONTENIDO).get().addOnSuccessListener { documents ->
            for (document in documents) {
                Log.d("DOC", "${document.id} => ${document.data}")
                val data = document.data
                list.add(
                    ContentInitModel(
                        id = data[ID].toString().toInt(),
                        title = data[TITLE].toString(),
                        synopsis = data[SYNOPSIS].toString(),
                        genres = data[GENRES].toString().split(",").toList(),
                        duration = data[DURATION].toString().toInt(),
                        releaseDate = data[RELEASE_DATE].toString(),
                        verticalImageUrl = data[VERTICAL_IMAGE_URL].toString(),
                        horizontalImageUrl = data[HORIZONTAL_IMAGE_URL].toString(),
                        rating = data[RATING].toString().toInt(),
                        platformsList = data[PLATFORMS_LIST].toString().split(",").toList(),
                        uploadDate = data[UPLOAD_DATE].toString().toLong(),
                        producersList = data[PRODUCERS_LIST].toString().split(",").toList(),
                        type = data[TYPE].toString(),
                        keywords = data[KEYWORDS].toString(),
                        isCamQuality = data[CALIDAD_CAM].toString().toBoolean()
                    )
                )
            }
        }
        return list
    }

//    return listOf(
//    ContentInitModel(
//    id = 1,
//    title = "title",
//    synopsis = "synopsis",
//    genres = listOf("Genero 1", "Genero 2"),
//    duration = 0,
//    releaseDate = "release date",
//    verticalImageUrl = "link vertical",
//    horizontalImageUrl = "link horizontal",
//    rating = 0,
//    platformsList = listOf("platform 1", "platform 2"),
//    uploadDate = 51541561651,
//    producersList = listOf("produccer 1", "produccer 2"),
//    type = "Pelicula",
//    keywords = "fgnilkren refgbrkjgbf jbrfgjkrb jrbfg jkr rfgjk k",
//    isCamQuality = false
//    ), ContentInitModel(
//    id = 2,
//    title = "title",
//    synopsis = "synopsis",
//    genres = listOf("Genero 1", "Genero 2"),
//    duration = 0,
//    releaseDate = "release date",
//    verticalImageUrl = "link vertical",
//    horizontalImageUrl = "link horizontal",
//    rating = 0,
//    platformsList = listOf("platform 1", "platform 2"),
//    uploadDate = 51541561651,
//    producersList = listOf("produccer 1", "produccer 2"),
//    type = "Pelicula",
//    keywords = "fgnilkren refgbrkjgbf jbrfgjkrb jrbfg jkr rfgjk k",
//    isCamQuality = false
//    ),
//    ContentInitModel(
//    id = 3,
//    title = "title",
//    synopsis = "synopsis",
//    genres = listOf("Genero 1", "Genero 2"),
//    duration = 0,
//    releaseDate = "release date",
//    verticalImageUrl = "link vertical",
//    horizontalImageUrl = "link horizontal",
//    rating = 0,
//    platformsList = listOf("platform 1", "platform 2"),
//    uploadDate = 51541561651,
//    producersList = listOf("produccer 1", "produccer 2"),
//    type = "Pelicula",
//    keywords = "fgnilkren refgbrkjgbf jbrfgjkrb jrbfg jkr rfgjk k",
//    isCamQuality = false
//    )
//    )
}