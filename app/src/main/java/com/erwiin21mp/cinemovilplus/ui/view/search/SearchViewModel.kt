package com.erwiin21mp.cinemovilplus.ui.view.search

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.erwiin21mp.cinemovilplus.core.ext.isNotNull
import com.erwiin21mp.cinemovilplus.core.ext.logData
import com.erwiin21mp.cinemovilplus.domain.model.ContentGenderModel
import com.erwiin21mp.cinemovilplus.domain.model.ContentSearchModel
import com.erwiin21mp.cinemovilplus.domain.model.GenderModel
import com.erwiin21mp.cinemovilplus.domain.model.Type.Movie
import com.erwiin21mp.cinemovilplus.domain.model.Type.Serie
import com.erwiin21mp.cinemovilplus.domain.usecase.GetMovieSearchUseCase
import com.erwiin21mp.cinemovilplus.domain.usecase.GetSerieSearchUseCase
import com.erwiin21mp.cinemovilplus.ui.view.home.HomeViewModel
import com.erwiin21mp.cinemovilplus.ui.view.home.HomeViewModel.Companion.CONTENT
import com.erwiin21mp.cinemovilplus.ui.view.home.HomeViewModel.Companion.GENDER
import com.erwiin21mp.cinemovilplus.ui.view.home.HomeViewModel.Companion.GENDERS
import com.erwiin21mp.cinemovilplus.ui.view.home.HomeViewModel.Companion.ID
import com.erwiin21mp.cinemovilplus.ui.view.home.HomeViewModel.Companion.ID_TMDB
import com.erwiin21mp.cinemovilplus.ui.view.home.HomeViewModel.Companion.IS_CAMERA_QUALITY
import com.erwiin21mp.cinemovilplus.ui.view.home.HomeViewModel.Companion.IS_ENABLED
import com.erwiin21mp.cinemovilplus.ui.view.home.HomeViewModel.Companion.UPLOAD_DATE
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val getMovieSearchUseCase: GetMovieSearchUseCase,
    private val getSerieSearchUseCase: GetSerieSearchUseCase
) : ViewModel() {
    private val db = FirebaseFirestore.getInstance()
    private val db2 = FirebaseFirestore.getInstance()
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
                                            tagline = result?.tagline
                                        )
                                    )
                                }
                            }

                            Serie -> {
                                val result =
                                    withContext(Dispatchers.IO) { getSerieSearchUseCase(idTmdb) }
                                if (result.isNotNull()) {
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
                                            createdBy = result?.createdBy.toString(),
                                            networks = result?.networks.toString(),
                                            tagline = result?.tagline,
                                            country = result?.country.toString()
                                        )
                                    )
                                }
                            }
                        }
                        listOfContent.postValue(listOfContentAux)
                        getGenders(listOfContentAux)
                    }
                }
            }
        }
    }

    private fun getGenders(list: MutableList<ContentSearchModel>) {
        logData("START ----------------------------------------------------------------------------------")
        val listOfGenders = mutableListOf<GenderModel>()
        val listOfContentGender = mutableListOf<ContentGenderModel>()
        db.collection(GENDERS).get().addOnSuccessListener { documents ->
            for (document in documents) {
                val data = document.data
                listOfGenders.add(
                    GenderModel(
                        id = data[ID].toString().toInt(),
                        gender = data[GENDER].toString()
                    )
                )
            }
            db2.collection(CONTENT_GENDER).get().addOnSuccessListener { docs ->
                for (doc in docs) {
                    val data2 = doc.data
                    listOfContentGender.add(
                        ContentGenderModel(
                            contentID = data2[CONTENT_ID].toString().toInt(),
                            genderID = data2[GENDER_ID].toString().toInt()
                        )
                    )
                    list.forEach { model ->
                        var gen = ""
                        listOfContentGender.filter { fil -> fil.contentID == model.id?.toInt() }
                            .forEach { model2 ->
                                gen = "$gen ${listOfGenders.filter { model2.genderID == it.id }}"
                            }
                        model.genres = gen
                        listOfContent.postValue(list)
                    }
                }
            }
        }
    }
}