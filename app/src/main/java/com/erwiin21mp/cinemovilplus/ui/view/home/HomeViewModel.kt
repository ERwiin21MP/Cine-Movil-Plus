package com.erwiin21mp.cinemovilplus.ui.view.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.erwiin21mp.cinemovilplus.core.ext.isNotNull
import com.erwiin21mp.cinemovilplus.domain.model.ContentHomeModel
import com.erwiin21mp.cinemovilplus.domain.model.GenderModel
import com.erwiin21mp.cinemovilplus.domain.model.ItemMXModel
import com.erwiin21mp.cinemovilplus.domain.usecase.GetDetailsMovieUseCase
import com.erwiin21mp.cinemovilplus.domain.usecase.GetDetailsSerieUserCase
import com.erwiin21mp.cinemovilplus.domain.usecase.GetWatchProvidersMovieUseCase
import com.erwiin21mp.cinemovilplus.domain.usecase.GetWatchProvidersSerieUseCase
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getDetailsMovieUseCase: GetDetailsMovieUseCase,
    private val getDetailsSerieUserCase: GetDetailsSerieUserCase,
    private val getWatchProvidersMovieUseCase: GetWatchProvidersMovieUseCase,
    private val getWatchProvidersSerieUseCase: GetWatchProvidersSerieUseCase
) :
    ViewModel() {

    private val db = FirebaseFirestore.getInstance()
    val listOfContent = MutableLiveData<List<ContentHomeModel>>(emptyList())
    val listOfPlatforms = MutableLiveData<List<ItemMXModel>>(emptyList())
    val listOfGenders = MutableLiveData<List<GenderModel>>(emptyList())
    val listAllContent = MutableLiveData<List<ContentHomeModel>>(emptyList())
    val listCurrentYear = MutableLiveData<List<ContentHomeModel>>(emptyList())
    val listCineMovilPlusNews = MutableLiveData<List<ContentHomeModel>>(emptyList())

    companion object {
        const val CONTENT = "content"
        const val ID = "id"
        const val ID_TMDB = "id_tmdb"
        const val TYPE_ID = "type_id"
        const val UPLOAD_DATE = "upload_date"
        const val IS_CAMERA_QUALITY = "is_camera_quality"
        const val IS_ENABLED = "is_enabled"
        const val GENDERS = "genders"
        const val GENDER = "gender"
        const val SERIE = "Serie"
        const val MOVIE = "Movie"
        const val IMAGE_URL = "image_url"
        const val ID_COLLECTION = "id_collection"
    }

    init {
        getContent()
        getGenders()
    }

    private fun getGenders() {
        val listOfGendersAux = mutableListOf<GenderModel>()
        db.collection(GENDERS).get().addOnSuccessListener { documents ->
            for (document in documents) {
                val data = document.data
                listOfGendersAux.add(
                    GenderModel(
                        id = data[ID].toString().toInt(),
                        gender = data[GENDER].toString(),
                        imageURL = data[IMAGE_URL].toString()
                    )
                )
                listOfGenders.postValue(listOfGendersAux)
            }
        }
    }

    private fun getContent() {
        val listOfContentAux = mutableListOf<ContentHomeModel>()
        val listOfPlatformsAux = mutableListOf<ItemMXModel>()
        db.collection(CONTENT).get().addOnSuccessListener { documents ->
            for (document in documents) {
                val data = document.data
                val isEnabled = data[IS_ENABLED].toString().toBoolean()
                val idTmdb = data[ID_TMDB].toString()

                if (isEnabled) {
                    val type = when (data[TYPE_ID].toString().toInt()) {
                        1 -> SERIE
                        2 -> MOVIE
                        else -> ""
                    }
                    viewModelScope.launch {
                        val result = when (type) {
                            SERIE -> withContext(Dispatchers.IO) { getDetailsSerieUserCase(idTmdb) }
                            MOVIE -> withContext(Dispatchers.IO) { getDetailsMovieUseCase(idTmdb) }
                            else -> null
                        }
                        if (result.isNotNull()) {
                            val id = data[ID].toString()
                            val isCameraQuality = data[IS_CAMERA_QUALITY].toString().toBoolean()
                            val uploadDate = data[UPLOAD_DATE].toString().toLong()
                            val idCollection = data[ID_COLLECTION].toString().toInt()
                            listOfContentAux.add(
                                ContentHomeModel(
                                    id = id,
                                    idTmdb = idTmdb,
                                    idCollection = idCollection,
                                    isCameraQuality = isCameraQuality,
                                    type = type,
                                    uploadDate = uploadDate,
                                    horizontalImageURL = result?.horizontalImageURL.orEmpty(),
                                    releaseDate = result?.releaseDate?.replace("-", "")?.toLong(),
                                    title = result?.title.orEmpty(),
                                    verticalImageURL = result?.verticalImageURL.orEmpty(),
                                )
                            )
                        }
                        when (type) {
                            SERIE -> {
                                val result2 = withContext(Dispatchers.IO) {
                                    getWatchProvidersSerieUseCase(idTmdb)
                                }
                                if (result2.isNotNull()) {
                                    result2!!.results?.mx?.flatrate?.forEach {
                                        listOfPlatformsAux.add(it)
                                    }
                                }
                            }

                            MOVIE -> {
                                val result2 = withContext(Dispatchers.IO) {
                                    getWatchProvidersMovieUseCase(idTmdb)
                                }
                                if (result2.isNotNull()) {
                                    result2!!.results?.mx?.apply {
                                        buy?.forEach { listOfPlatformsAux.add(it) }
                                        rent?.forEach { listOfPlatformsAux.add(it) }
                                        flatrate?.forEach { listOfPlatformsAux.add(it) }
                                    }
                                }
                            }

                            else -> null
                        }
                        listOfContent.postValue(listOfContentAux)
                        listOfPlatforms.postValue(listOfPlatformsAux.distinct())
                        listAllContent.postValue(listOfContentAux.shuffled())
                        getCurrentYear(listOfContentAux)
                        getCineMovilPlusNews(listOfContentAux)
                    }
                }
            }
        }
    }

    private fun getCineMovilPlusNews(list: MutableList<ContentHomeModel>) {
        list.sortByDescending { it.uploadDate }
        listCineMovilPlusNews.postValue(list)
    }

    private fun getCurrentYear(list: MutableList<ContentHomeModel>) {
        list.forEach { it.releaseDate = it.releaseDate.toString().substring(0, 4).toLong() }
        list.sortBy { it.releaseDate }
        listCurrentYear.postValue(list.filter { it.releaseDate == list.last().releaseDate })
    }
}