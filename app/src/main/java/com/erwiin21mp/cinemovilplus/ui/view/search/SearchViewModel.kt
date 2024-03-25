package com.erwiin21mp.cinemovilplus.ui.view.search

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.erwiin21mp.cinemovilplus.data.network.firebase.FirestoreManager
import com.erwiin21mp.cinemovilplus.domain.model.ContentModel
import com.erwiin21mp.cinemovilplus.domain.model.ContentSearchModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val db: FirestoreManager
) : ViewModel() {
    val listOfContent = MutableLiveData<List<ContentModel>>(emptyList())

    companion object {
        const val KEYWORDS = "keywords"
        const val TABLE_CONTENT_GENDER = "content_gender"
        const val CONTENT_ID = "content_id"
        const val GENDER_ID = "gender_id"
    }

    init {

    }

//    private fun getContent() {
//        val listOfContentAux = mutableListOf<ContentSearchModel>()
//
//        FirebaseFirestore.getInstance().collection(TABLE_CONTENT).get()
//            .addOnSuccessListener { documents ->
//                for (document in documents) {
//                    val data = document.data
//                    val isEnabled = data[IS_ENABLED].toString().toBoolean()
//                    val idTmdb = data[ID_TMDB].toString()
//
//                    if (isEnabled) {
//                        val typeAux = data[HomeViewModel.TYPE_ID].toString().toInt()
//                        val type = if (typeAux == 1) Serie else Movie
//
//                        viewModelScope.launch {
//                            val id = data[ID].toString()
//                            when (type) {
//                                Movie -> {
//                                    val result =
//                                        withContext(Dispatchers.IO) { getMovieSearchUseCase(idTmdb) }
//                                    if (result.isNotNull()) {
//                                        val result2 = withContext(Dispatchers.IO) {
//                                            getWatchProvidersMovieUseCase(idTmdb)
//                                        }
//                                        if (result2.isNotNull())
//                                            listOfContentAux.add(
//                                                ContentSearchModel(
//                                                    id = id,
//                                                    keywords = data[KEYWORDS].toString(),
//                                                    genres = "",
//                                                    type = type,
//                                                    uploadDate = data[UPLOAD_DATE].toString()
//                                                        .toLong(),
//                                                    isCameraQuality = data[IS_CAMERA_QUALITY].toString()
//                                                        .toBoolean(),
//                                                    title = result?.title,
//                                                    originalTitle = result?.originalTitle,
//                                                    synopsis = result?.synopsis,
//                                                    productionCompanies = result?.productionCompanies.toString(),
//                                                    productionCountries = result?.productionCountries.toString(),
//                                                    releaseDate = result?.releaseDate,
//                                                    verticalImageURL = result?.verticalImageURL,
//                                                    tagline = result?.tagline,
//                                                    platforms = result2?.results?.mx?.flatrate?.joinToString(
//                                                        separator = ", "
//                                                    ) { it.name.toString() }
//                                                )
//                                            )
//                                    }
//                                }
//
//                                Serie -> {
//                                    val result =
//                                        withContext(Dispatchers.IO) { getSerieSearchUseCase(idTmdb) }
//                                    if (result.isNotNull()) {
//                                        val result2 = withContext(Dispatchers.IO) {
//                                            getWatchProvidersSerieUseCase(idTmdb)
//                                        }
//                                        if (result2.isNotNull())
//                                            listOfContentAux.add(
//                                                ContentSearchModel(
//                                                    id = id,
//                                                    keywords = data[KEYWORDS].toString(),
//                                                    genres = "",
//                                                    type = type,
//                                                    uploadDate = data[UPLOAD_DATE].toString()
//                                                        .toLong(),
//                                                    isCameraQuality = data[IS_CAMERA_QUALITY].toString()
//                                                        .toBoolean(),
//                                                    title = result?.title,
//                                                    originalTitle = result?.originalTitle,
//                                                    synopsis = result?.synopsis,
//                                                    productionCompanies = result?.productionCompanies,
//                                                    productionCountries = result?.productionCountries,
//                                                    releaseDate = result?.releaseDate,
//                                                    verticalImageURL = result?.verticalImageURL,
//                                                    createdBy = result?.createdBy.toString(),
//                                                    networks = result?.networks.toString(),
//                                                    tagline = result?.tagline,
//                                                    country = result?.country.toString(),
//                                                    platforms = result2?.results?.mx?.flatrate?.joinToString(
//                                                        separator = ", "
//                                                    ) { it.name.toString() }
//                                                )
//                                            )
//                                    }
//                                }
//                            }
//                            listOfContentAux.sortByDescending { it.uploadDate }
//                            listOfContent.postValue(listOfContentAux)
//                            getGenders(listOfContentAux)
//                        }
//                    }
//                }
//            }
//    }

    private fun getGenders(list: MutableList<ContentSearchModel>) {
        viewModelScope.launch {
            val listOfGenders = db.getGenders()
            val listOfContentGender = db.getContentGenders()
//            logList(listOfGenders.sortedBy { it.id }, "listOfGenders")
            list.forEach { model ->
                model.genres = listOfContentGender
                    .filter { it.contentID == model.id?.toInt() }
                    .joinToString(separator = " ") { contentGender ->
                        listOfGenders.find { it.id == contentGender.genderID }?.gender.orEmpty()
                    }
            }

//            listOfContent.postValue(list)
        }
    }
}