package com.erwiin21mp.cinemovilplus.data.network.firebase

import com.erwiin21mp.cinemovilplus.core.ext.isNotNull
import com.erwiin21mp.cinemovilplus.domain.model.ContentGenderModel
import com.erwiin21mp.cinemovilplus.domain.model.ContentModel
import com.erwiin21mp.cinemovilplus.domain.model.FlatrateModel
import com.erwiin21mp.cinemovilplus.domain.model.GenderModel
import com.erwiin21mp.cinemovilplus.domain.model.Type.Movie
import com.erwiin21mp.cinemovilplus.domain.model.Type.Serie
import com.erwiin21mp.cinemovilplus.domain.usecase.GetDetailsMovieUseCase
import com.erwiin21mp.cinemovilplus.domain.usecase.GetDetailsSerieUserCase
import com.erwiin21mp.cinemovilplus.domain.usecase.GetWatchProvidersMovieUseCase
import com.erwiin21mp.cinemovilplus.domain.usecase.GetWatchProvidersSerieUseCase
import com.erwiin21mp.cinemovilplus.ui.view.search.SearchViewModel
import com.erwiin21mp.cinemovilplus.ui.view.search.SearchViewModel.Companion.KEYWORDS
import com.erwiin21mp.cinemovilplus.ui.view.search.SearchViewModel.Companion.TABLE_CONTENT_GENDER
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
    private val getWatchProvidersSerieUseCase: GetWatchProvidersSerieUseCase
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
                contentID = data[SearchViewModel.CONTENT_ID].toString().toInt(),
                genderID = data[SearchViewModel.GENDER_ID].toString().toInt()
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
}