package com.erwiin21mp.cinemovilplus.data.network.firebase

import com.erwiin21mp.cinemovilplus.core.ext.isNotNull
import com.erwiin21mp.cinemovilplus.domain.model.CollectionModel
import com.erwiin21mp.cinemovilplus.domain.model.ContentGenderModel
import com.erwiin21mp.cinemovilplus.domain.model.ContentModel
import com.erwiin21mp.cinemovilplus.domain.model.FlatrateModel
import com.erwiin21mp.cinemovilplus.domain.model.GenderModel
import com.erwiin21mp.cinemovilplus.domain.model.Type.Movie
import com.erwiin21mp.cinemovilplus.domain.model.Type.Serie
import com.erwiin21mp.cinemovilplus.domain.usecase.GetCollectionDetailsUseCase
import com.erwiin21mp.cinemovilplus.domain.usecase.GetDetailsMovieUseCase
import com.erwiin21mp.cinemovilplus.domain.usecase.GetDetailsSerieUserCase
import com.erwiin21mp.cinemovilplus.domain.usecase.GetWatchProvidersMovieUseCase
import com.erwiin21mp.cinemovilplus.domain.usecase.GetWatchProvidersSerieUseCase
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

class FirestoreManager @Inject constructor(
    private val db: FirebaseFirestore,
    private val getDetailsMovieUseCase: GetDetailsMovieUseCase,
    private val getDetailsSerieUserCase: GetDetailsSerieUserCase,
    private val getWatchProvidersMovieUseCase: GetWatchProvidersMovieUseCase,
    private val getWatchProvidersSerieUseCase: GetWatchProvidersSerieUseCase,
    private val getCollectionDetailsUseCase: GetCollectionDetailsUseCase
) {
    companion object {
        const val CONTENT_URL = "content_url"
        const val TABLE_CONTENT = "content"
        const val ID = "id"
        const val ID_TMDB = "id_tmdb"
        const val TYPE_ID = "type_id"
        const val UPLOAD_DATE = "upload_date"
        const val IS_CAMERA_QUALITY = "is_camera_quality"
        const val IS_ENABLED = "is_enabled"
        const val GENDERS = "genders"
        const val GENDER = "gender"
        const val IMAGE_URL = "image_url"
        const val ID_COLLECTION = "id_collection"
        const val KEYWORDS = "keywords"
        const val TABLE_CONTENT_GENDER = "content_gender"
        const val CONTENT_ID = "content_id"
        const val GENDER_ID = "gender_id"
    }

    private fun getTypeOfContent(idType: Int) = if (idType == 1) Serie else Movie

    suspend fun getGenders(): List<GenderModel> {
        return db.collection(GENDERS).get().await().documents.map { document ->
            val data = document.data!!
            GenderModel(
                id = data[ID].toString().toInt(),
                gender = data[GENDER].toString(),
                imageURL = data[IMAGE_URL].toString()
            )
        }
    }

    suspend fun getContentGenders() = db.collection(TABLE_CONTENT_GENDER).get()
        .await().documents.map { document ->
            val data = document.data!!
            ContentGenderModel(
                contentID = data[CONTENT_ID].toString().toInt(),
                genderID = data[GENDER_ID].toString().toInt()
            )
        }.shuffled()

    private suspend fun getContentByFirebase() =
        db.collection(TABLE_CONTENT).get().await().documents.map { document ->
            val data = document.data!!
            ContentModel(
                contentURL = data[CONTENT_URL].toString(),
                id = data[ID].toString().toInt(),
                idCollection = data[ID_COLLECTION].toString().toInt(),
                idTmdb = data[ID_TMDB].toString().toInt(),
                isCameraQuality = data[IS_CAMERA_QUALITY].toString().toBoolean(),
                isEnabled = data[IS_ENABLED].toString().toBoolean(),
                keywords = data[KEYWORDS].toString(),
                type = getTypeOfContent(data[TYPE_ID].toString().toInt()),
                uploadDate = data[UPLOAD_DATE].toString().toLong()
            )
        }.shuffled()

    suspend fun getContentHome(): List<ContentModel> {
        val listRet = mutableListOf<ContentModel>()
        getContentByFirebase().forEach {
            if (it.isEnabled == true) {
                val item = it
                val result = when (it.type) {
                    Movie -> withContext(Dispatchers.IO) { getDetailsMovieUseCase(it.idTmdb.toString()) }
                    Serie -> withContext(Dispatchers.IO) { getDetailsSerieUserCase(it.idTmdb.toString()) }
                }
                if (result.isNotNull()) {
                    item.horizontalImageURL = result?.horizontalImageURL.orEmpty()
                    item.title = result?.title.orEmpty()
                    item.verticalImageURL = result?.verticalImageURL.orEmpty()
                    item.releaseDate = result?.releaseDate
                }
                listRet.add(item)
            }
        }
        return listRet
    }

    suspend fun getPlatforms(list: List<ContentModel>): List<FlatrateModel> {
        val listRet = mutableListOf<FlatrateModel>()
        list.forEach { content ->
            val result = when (content.type) {
                Movie -> withContext(Dispatchers.IO) { getWatchProvidersMovieUseCase(content.idTmdb.toString()) }
                Serie -> withContext(Dispatchers.IO) { getWatchProvidersSerieUseCase(content.idTmdb.toString()) }
            }
            if (result.isNotNull()) {
                result!!.results?.mx?.flatrate?.forEach { item ->
                    listRet.add(
                        FlatrateModel(
                            imageURL = item.imageURL,
                            name = item.name,
                            displayPriority = item.displayPriority
                        )
                    )
                }
            }
        }
        return listRet.sortedBy { it.displayPriority }.distinct()
    }

    suspend fun getCollections(list: List<ContentModel>): List<CollectionModel> {
        val listOfCollectionsAux = mutableListOf<CollectionModel>()

        list.forEach {
            val result =
                withContext(Dispatchers.IO) { getCollectionDetailsUseCase(it.idCollection.toString()) }
            if (result.isNotNull()) {
                listOfCollectionsAux.add(
                    CollectionModel(
                        id = result?.id,
                        name = result?.name,
                        verticalImageURL = result?.verticalImageURL,
                        horizontalImageURL = result?.horizontalImageURL
                    )
                )
            }
        }
        return listOfCollectionsAux
    }

//    suspend fun getContentSearch(): List<ContentModel> {
//        val list = mutableListOf<ContentModel>()
//        getContentByFirebase().forEach {
//            if (it.isEnabled == true) {
//                val item = it
//                val result = when (it.type) {
//                    Movie -> {
//                        val result =
//                            withContext(Dispatchers.IO) { getMovieSearchUseCase(it.idTmdb.toString()) }
//                        if (result.isNotNull()) {
//                            val result2 =
//                                withContext(Dispatchers.IO) { getWatchProvidersMovieUseCase(it.idTmdb.toString()) }
//                            if (result2.isNotNull()) {
//                                val item = it
//                                item.apply {
//                                    releaseDate = result?.releaseDate?.toLong()
//                                    verticalImageURL
//                                    title
//                                    genres
//                                    synopsis
//                                    productionCountries
//                                    originalTitle
//                                    productionCompanies
//                                    createdBy
//                                    networks
//                                    tagline
//                                    country
//                                    platforms
//                                }
//                            }
//                            list.add(
//                                ContentModel(
//                                    id = id,
//                                    keywords = data[KEYWORDS].toString(),
//                                    genres = "",
//                                    type = type,
//                                    uploadDate = data[UPLOAD_DATE].toString()
//                                        .toLong(),
//                                    isCameraQuality = data[IS_CAMERA_QUALITY].toString()
//                                        .toBoolean(),
//                                    title = result?.title,
//                                    originalTitle = result?.originalTitle,
//                                    synopsis = result?.synopsis,
//                                    productionCompanies = result?.productionCompanies.toString(),
//                                    productionCountries = result?.productionCountries.toString(),
//                                    releaseDate = result?.releaseDate,
//                                    verticalImageURL = result?.verticalImageURL,
//                                    tagline = result?.tagline,
//                                    platforms = result2?.results?.mx?.flatrate?.joinToString(
//                                        separator = ", "
//                                    ) { it.name.toString() }
//                                )
//                            )
//                        }
//                    }
//
//                    Serie -> {
//                        val result =
//                            withContext(Dispatchers.IO) { getSerieSearchUseCase(idTmdb) }
//                        if (result.isNotNull()) {
//                            val result2 = withContext(Dispatchers.IO) {
//                                getWatchProvidersSerieUseCase(idTmdb)
//                            }
//                            if (result2.isNotNull())
//                                listOfContentAux.add(
//                                    ContentSearchModel(
//                                        id = id,
//                                        keywords = data[KEYWORDS].toString(),
//                                        genres = "",
//                                        type = type,
//                                        uploadDate = data[UPLOAD_DATE].toString()
//                                            .toLong(),
//                                        isCameraQuality = data[IS_CAMERA_QUALITY].toString()
//                                            .toBoolean(),
//                                        title = result?.title,
//                                        originalTitle = result?.originalTitle,
//                                        synopsis = result?.synopsis,
//                                        productionCompanies = result?.productionCompanies,
//                                        productionCountries = result?.productionCountries,
//                                        releaseDate = result?.releaseDate,
//                                        verticalImageURL = result?.verticalImageURL,
//                                        createdBy = result?.createdBy.toString(),
//                                        networks = result?.networks.toString(),
//                                        tagline = result?.tagline,
//                                        country = result?.country.toString(),
//                                        platforms = result2?.results?.mx?.flatrate?.joinToString(
//                                            separator = ", "
//                                        ) { it.name.toString() }
//                                    )
//                                )
//                        }
//
//                        list.add(item)
//                    }
//                }
//                return list
//            }
//        }
//    }
}