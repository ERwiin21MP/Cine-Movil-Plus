package com.erwiin21mp.cinemovilplus.ui.view.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.erwiin21mp.cinemovilplus.core.ext.isNotNull
import com.erwiin21mp.cinemovilplus.data.network.firebase.FirestoreManager
import com.erwiin21mp.cinemovilplus.domain.model.CollectionModel
import com.erwiin21mp.cinemovilplus.domain.model.ContentHomeModel
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
    private val getWatchProvidersSerieUseCase: GetWatchProvidersSerieUseCase,
    private val getCollectionDetailsUseCase: GetCollectionDetailsUseCase,
    private val firestore: FirestoreManager
) : ViewModel() {

    private val db = FirebaseFirestore.getInstance()
    val listOfContent = MutableLiveData<List<ContentHomeModel>>(emptyList())
    val listOfPlatforms = MutableLiveData<List<FlatrateModel>>(emptyList())
    val listOfGenders = MutableLiveData<List<GenderModel>>(emptyList())
    val listAllContent = MutableLiveData<List<ContentHomeModel>>(emptyList())
    val listCurrentYear = MutableLiveData<List<ContentHomeModel>>(emptyList())
    val listCineMovilPlusNews = MutableLiveData<List<ContentHomeModel>>(emptyList())
    val listOfCollections = MutableLiveData<List<CollectionModel>>(emptyList())

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
        const val IMAGE_URL = "image_url"
        const val ID_COLLECTION = "id_collection"
    }

    init {
        getContent()
        getGenders()
    }

    private fun getGenders() {
        viewModelScope.launch {
            val list = withContext(Dispatchers.IO) { firestore.getGenders().shuffled() }
            listOfGenders.postValue(list)
        }
    }

    private fun getContent() {
        val listOfContentAux = mutableListOf<ContentHomeModel>()
        val listOfPlatformsAux = mutableListOf<FlatrateModel>()
        db.collection(CONTENT).get().addOnSuccessListener { documents ->
            for (document in documents) {
                val data = document.data
                val isEnabled = data[IS_ENABLED].toString().toBoolean()
                val idTmdb = data[ID_TMDB].toString()

                if (isEnabled) {
                    val typeAux = data[TYPE_ID].toString().toInt()
                    val type = if (typeAux == 1) Serie else Movie

                    viewModelScope.launch {
                        val result = when (type) {
                            Movie -> withContext(Dispatchers.IO) { getDetailsMovieUseCase(idTmdb) }
                            Serie -> withContext(Dispatchers.IO) { getDetailsSerieUserCase(idTmdb) }
                        }
                        if (result.isNotNull()) {
                            listOfContentAux.add(
                                ContentHomeModel(
                                    id = data[ID].toString(),
                                    idTmdb = idTmdb,
                                    idCollection = data[ID_COLLECTION].toString(),
                                    isCameraQuality = data[IS_CAMERA_QUALITY].toString()
                                        .toBoolean(),
                                    type = type,
                                    uploadDate = data[UPLOAD_DATE].toString().toLong(),
                                    horizontalImageURL = result?.horizontalImageURL.orEmpty(),
                                    releaseDate = result?.releaseDate?.replace("-", "")?.toLong(),
                                    title = result?.title.orEmpty(),
                                    verticalImageURL = result?.verticalImageURL.orEmpty(),
                                )
                            )
                        }
                        val result2 = when (type) {
                            Movie -> withContext(Dispatchers.IO) {
                                getWatchProvidersMovieUseCase(idTmdb)
                            }

                            Serie -> withContext(Dispatchers.IO) {
                                getWatchProvidersSerieUseCase(idTmdb)
                            }
                        }
                        if (result2.isNotNull()) {
                            result2?.results?.mx?.flatrate?.forEach { listOfPlatformsAux.add(it) }
                        }
                        listOfContent.postValue(listOfContentAux)
                        listOfPlatforms.postValue(
                            listOfPlatformsAux.distinct().sortedBy { it.displayPriority })
                        listAllContent.postValue(listOfContentAux.shuffled())
                        getCurrentYear(listOfContentAux)
                        getCineMovilPlusNews(listOfContentAux)
                        getCollections(listOfContentAux.filter { it.idCollection?.toInt() != 0 })
                    }
                }
            }
        }
    }

    private fun getCollections(list: List<ContentHomeModel>) {
        viewModelScope.launch {
            val listOfCollectionsAux = mutableListOf<CollectionModel>()

            list.forEach {
                val result =
                    withContext(Dispatchers.IO) { getCollectionDetailsUseCase(it.idCollection!!) }
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
            listOfCollections.postValue(listOfCollectionsAux)
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