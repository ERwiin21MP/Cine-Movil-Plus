package com.erwiin21mp.cinemovilplus.ui.view.search

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.erwiin21mp.cinemovilplus.core.ext.isNotNull
import com.erwiin21mp.cinemovilplus.domain.model.CollectionModel
import com.erwiin21mp.cinemovilplus.domain.model.ContentHomeModel
import com.erwiin21mp.cinemovilplus.domain.model.FlatrateModel
import com.erwiin21mp.cinemovilplus.domain.model.Type
import com.erwiin21mp.cinemovilplus.domain.usecase.GetCollectionDetailsUseCase
import com.erwiin21mp.cinemovilplus.domain.usecase.GetDetailsMovieUseCase
import com.erwiin21mp.cinemovilplus.domain.usecase.GetDetailsSerieUserCase
import com.erwiin21mp.cinemovilplus.domain.usecase.GetWatchProvidersMovieUseCase
import com.erwiin21mp.cinemovilplus.domain.usecase.GetWatchProvidersSerieUseCase
import com.erwiin21mp.cinemovilplus.ui.view.home.HomeViewModel
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val getDetailsMovieUseCase: GetDetailsMovieUseCase,
    private val getDetailsSerieUserCase: GetDetailsSerieUserCase,
    private val getWatchProvidersMovieUseCase: GetWatchProvidersMovieUseCase,
    private val getWatchProvidersSerieUseCase: GetWatchProvidersSerieUseCase,
    private val getCollectionDetailsUseCase: GetCollectionDetailsUseCase
) : ViewModel() {
    private val db = FirebaseFirestore.getInstance()
    val listOfContent = MutableLiveData<List<ContentHomeModel>>(emptyList())
    val listOfPlatforms = MutableLiveData<List<FlatrateModel>>(emptyList())
    val listAllContent = MutableLiveData<List<ContentHomeModel>>(emptyList())
    val listOfCollections = MutableLiveData<List<CollectionModel>>(emptyList())

    init {
        getContent()
    }

    private fun getContent() {
        val listOfContentAux = mutableListOf<ContentHomeModel>()
        val listOfPlatformsAux = mutableListOf<FlatrateModel>()
        db.collection(HomeViewModel.CONTENT).get().addOnSuccessListener { documents ->
            for (document in documents) {
                val data = document.data
                val isEnabled = data[HomeViewModel.IS_ENABLED].toString().toBoolean()
                val idTmdb = data[HomeViewModel.ID_TMDB].toString()

                if (isEnabled) {
                    val typeAux = data[HomeViewModel.TYPE_ID].toString().toInt()
                    val type = if (typeAux == 1) Type.Serie else Type.Movie

                    viewModelScope.launch {
                        val result = when (type) {
                            Type.Movie -> withContext(Dispatchers.IO) {
                                getDetailsMovieUseCase(
                                    idTmdb
                                )
                            }

                            Type.Serie -> withContext(Dispatchers.IO) {
                                getDetailsSerieUserCase(
                                    idTmdb
                                )
                            }
                        }
                        if (result.isNotNull()) {
                            listOfContentAux.add(
                                ContentHomeModel(
                                    id = data[HomeViewModel.ID].toString(),
                                    idTmdb = idTmdb,
                                    idCollection = data[HomeViewModel.ID_COLLECTION].toString(),
                                    isCameraQuality = data[HomeViewModel.IS_CAMERA_QUALITY].toString()
                                        .toBoolean(),
                                    type = type,
                                    uploadDate = data[HomeViewModel.UPLOAD_DATE].toString()
                                        .toLong(),
                                    horizontalImageURL = result?.horizontalImageURL.orEmpty(),
                                    releaseDate = result?.releaseDate?.replace("-", "")?.toLong(),
                                    title = result?.title.orEmpty(),
                                    verticalImageURL = result?.verticalImageURL.orEmpty(),
                                )
                            )
                        }
                        val result2 = when (type) {
                            Type.Movie -> withContext(Dispatchers.IO) {
                                getWatchProvidersMovieUseCase(idTmdb)
                            }

                            Type.Serie -> withContext(Dispatchers.IO) {
                                getWatchProvidersSerieUseCase(idTmdb)
                            }
                        }
                        if (result2.isNotNull()) {
                            result2?.results?.mx?.flatrate?.forEach { listOfPlatformsAux.add(it) }
                        }
                        listOfContent.postValue(listOfContentAux)
                        listOfPlatforms.postValue(listOfPlatformsAux.distinct().sortedBy { it.displayPriority })
                        listAllContent.postValue(listOfContentAux.shuffled())
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
}