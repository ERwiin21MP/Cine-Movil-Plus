package com.erwiin21mp.cinemovilplus.ui.view.search

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.erwiin21mp.cinemovilplus.core.ext.isNotNull
import com.erwiin21mp.cinemovilplus.data.network.firebase.FirestoreManager
import com.erwiin21mp.cinemovilplus.domain.model.ContentGenderModel
import com.erwiin21mp.cinemovilplus.domain.model.ContentSearchModel
import com.erwiin21mp.cinemovilplus.domain.model.Type.Movie
import com.erwiin21mp.cinemovilplus.domain.model.Type.Serie
import com.erwiin21mp.cinemovilplus.domain.usecase.GetMovieSearchUseCase
import com.erwiin21mp.cinemovilplus.domain.usecase.GetSerieSearchUseCase
import com.erwiin21mp.cinemovilplus.domain.usecase.GetWatchProvidersMovieUseCase
import com.erwiin21mp.cinemovilplus.domain.usecase.GetWatchProvidersSerieUseCase
import com.erwiin21mp.cinemovilplus.ui.view.home.HomeViewModel
import com.erwiin21mp.cinemovilplus.ui.view.home.HomeViewModel.Companion.CONTENT
import com.erwiin21mp.cinemovilplus.ui.view.home.HomeViewModel.Companion.ID
import com.erwiin21mp.cinemovilplus.ui.view.home.HomeViewModel.Companion.ID_TMDB
import com.erwiin21mp.cinemovilplus.ui.view.home.HomeViewModel.Companion.IS_CAMERA_QUALITY
import com.erwiin21mp.cinemovilplus.ui.view.home.HomeViewModel.Companion.IS_ENABLED
import com.erwiin21mp.cinemovilplus.ui.view.home.HomeViewModel.Companion.UPLOAD_DATE
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val getMovieSearchUseCase: GetMovieSearchUseCase,
    private val getSerieSearchUseCase: GetSerieSearchUseCase,
    private val getWatchProvidersSerieUseCase: GetWatchProvidersSerieUseCase,
    private val getWatchProvidersMovieUseCase: GetWatchProvidersMovieUseCase,
    private val firestore: FirestoreManager
) : ViewModel() {
    private val db = FirebaseFirestore.getInstance()
    val listOfContent = MutableLiveData<List<ContentSearchModel>>(emptyList())

    companion object {
        const val KEYWORDS = "keywords"
        const val CONTENT_GENDER = "content_gender"
        const val CONTENT_ID = "content_id"
        const val GENDER_ID = "gender_id"
    }

    init {
        getContent()
    }

    private fun getContent() {
        val listOfContentAux = mutableListOf<ContentSearchModel>()

        db.collection(CONTENT).get().addOnSuccessListener { documents ->
            for (document in documents) {
                val data = document.data
                val isEnabled = data[IS_ENABLED].toString().toBoolean()
                val idTmdb = data[ID_TMDB].toString()

                if (isEnabled) {
                    val typeAux = data[HomeViewModel.TYPE_ID].toString().toInt()
                    val type = if (typeAux == 1) Serie else Movie

                    viewModelScope.launch {
                        val id = data[ID].toString()
                        when (type) {
                            Movie -> {
                                val result =
                                    withContext(Dispatchers.IO) { getMovieSearchUseCase(idTmdb) }
                                if (result.isNotNull()) {
                                    val result2 = withContext(Dispatchers.IO) {
                                        getWatchProvidersMovieUseCase(idTmdb)
                                    }
                                    if (result2.isNotNull())
                                        listOfContentAux.add(
                                            ContentSearchModel(
                                                id = id,
                                                keywords = data[KEYWORDS].toString(),
                                                genres = "",
                                                type = type,
                                                uploadDate = data[UPLOAD_DATE].toString().toLong(),
                                                isCameraQuality = data[IS_CAMERA_QUALITY].toString()
                                                    .toBoolean(),
                                                title = result?.title,
                                                originalTitle = result?.originalTitle,
                                                synopsis = result?.synopsis,
                                                productionCompanies = result?.productionCompanies.toString(),
                                                productionCountries = result?.productionCountries.toString(),
                                                releaseDate = result?.releaseDate,
                                                verticalImageURL = result?.verticalImageURL,
                                                tagline = result?.tagline,
                                                platforms = result2?.results?.mx?.flatrate?.joinToString(
                                                    separator = ", "
                                                ) { it.name.toString() }
                                            )
                                        )
                                }
                            }

                            Serie -> {
                                val result =
                                    withContext(Dispatchers.IO) { getSerieSearchUseCase(idTmdb) }
                                if (result.isNotNull()) {
                                    val result2 = withContext(Dispatchers.IO) {
                                        getWatchProvidersSerieUseCase(idTmdb)
                                    }
                                    if (result2.isNotNull())
                                        listOfContentAux.add(
                                            ContentSearchModel(
                                                id = id,
                                                keywords = data[KEYWORDS].toString(),
                                                genres = "",
                                                type = type,
                                                uploadDate = data[UPLOAD_DATE].toString().toLong(),
                                                isCameraQuality = data[IS_CAMERA_QUALITY].toString()
                                                    .toBoolean(),
                                                title = result?.title,
                                                originalTitle = result?.originalTitle,
                                                synopsis = result?.synopsis,
                                                productionCompanies = result?.productionCompanies,
                                                productionCountries = result?.productionCountries,
                                                releaseDate = result?.releaseDate,
                                                verticalImageURL = result?.verticalImageURL,
                                                createdBy = result?.createdBy.toString(),
                                                networks = result?.networks.toString(),
                                                tagline = result?.tagline,
                                                country = result?.country.toString(),
                                                platforms = result2?.results?.mx?.flatrate?.joinToString(
                                                    separator = ", "
                                                ) { it.name.toString() }
                                            )
                                        )
                                }
                            }
                        }
                        listOfContentAux.sortByDescending { it.uploadDate }
                        listOfContent.postValue(listOfContentAux)
                        getGenders(listOfContentAux)
                    }
                }
            }
        }
    }

    private suspend fun getContentGenders(): List<ContentGenderModel> {
        return db.collection(CONTENT_GENDER).get().await().documents.map { document ->
            val data = document.data!!
            ContentGenderModel(
                contentID = data[CONTENT_ID].toString().toInt(),
                genderID = data[GENDER_ID].toString().toInt()
            )
        }
    }

    private fun getGenders(list: MutableList<ContentSearchModel>) {
        viewModelScope.launch {
            val genders = async { firestore.getGenders() }
            val contentGenders = async { getContentGenders() }

            val listOfGenders = genders.await()
            val listOfContentGender = contentGenders.await()

            list.forEach { model ->
                model.genres = listOfContentGender
                    .filter { it.contentID == model.id?.toInt() }
                    .joinToString(separator = " ") { contentGender ->
                        listOfGenders.find { it.id == contentGender.genderID }?.gender.orEmpty()
                    }
            }

            listOfContent.postValue(list)
        }
    }
}