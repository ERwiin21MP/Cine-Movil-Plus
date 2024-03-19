package com.erwiin21mp.cinemovilplus.ui.view.search

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.erwiin21mp.cinemovilplus.core.ext.isNotNull
import com.erwiin21mp.cinemovilplus.domain.model.CollectionModel
import com.erwiin21mp.cinemovilplus.domain.model.ContentHomeModel
import com.erwiin21mp.cinemovilplus.domain.model.ContentSearchModel
import com.erwiin21mp.cinemovilplus.domain.model.FlatrateModel
import com.erwiin21mp.cinemovilplus.domain.model.Type.*
import com.erwiin21mp.cinemovilplus.domain.usecase.GetCollectionDetailsUseCase
import com.erwiin21mp.cinemovilplus.domain.usecase.GetDetailsMovieUseCase
import com.erwiin21mp.cinemovilplus.domain.usecase.GetDetailsSerieUserCase
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
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val getDetailsMovieUseCase: GetDetailsMovieUseCase,
    private val getDetailsSerieUserCase: GetDetailsSerieUserCase,
    private val getWatchProvidersMovieUseCase: GetWatchProvidersMovieUseCase,
    private val getWatchProvidersSerieUseCase: GetWatchProvidersSerieUseCase,
    private val getCollectionDetailsUseCase: GetCollectionDetailsUseCase,
    private val getMovieSearchUseCase: GetMovieSearchUseCase,
    private val getSerieSearchUseCase: GetSerieSearchUseCase
) : ViewModel() {
    private val db = FirebaseFirestore.getInstance()
    val listOfContent = MutableLiveData<List<ContentSearchModel>>(emptyList())
    val listOfPlatforms = MutableLiveData<List<FlatrateModel>>(emptyList())
    val listOfCollections = MutableLiveData<List<CollectionModel>>(emptyList())

    companion object {
        const val KEYWORDS = "keywords"
    }

    init {
        getContent()
    }

    private fun getContent() {
        val listOfContentAux = mutableListOf<ContentSearchModel>()
        val listOfPlatformsAux = mutableListOf<FlatrateModel>()

        db.collection(CONTENT).get().addOnSuccessListener { documents ->
            for (document in documents) {
                val data = document.data
                val isEnabled = data[IS_ENABLED].toString().toBoolean()
                val idTmdb = data[ID_TMDB].toString()

                if (isEnabled) {
                    val typeAux = data[HomeViewModel.TYPE_ID].toString().toInt()
                    val type = if (typeAux == 1) Serie else Movie

                    viewModelScope.launch {
                        when (type) {
                            Movie -> {
                                val call =
                                    withContext(Dispatchers.IO) { getMovieSearchUseCase(idTmdb) }
                                if (call.isNotNull()) {
                                    listOfContentAux.add(
                                        ContentSearchModel(
                                            id = data[ID].toString(),
                                            keywords = data[KEYWORDS].toString(),
                                            genres = "",
                                            type = type,
                                            uploadDate = data[UPLOAD_DATE].toString().toLong(),
                                            isCameraQuality = data[IS_CAMERA_QUALITY].toString()
                                                .toBoolean(),
                                            title = call?.title,
                                            originalTitle = call?.originalTitle,
                                            synopsis = call?.synopsis,
                                            productionCompanies = call?.productionCompanies.toString(),
                                            productionCountries = call?.productionCountries.toString()
                                        )
                                    )
                                }
                            }

                            Serie -> {}
                        }
                        val result = when (type) {
                            Movie -> withContext(Dispatchers.IO) {
                                getDetailsMovieUseCase(
                                    idTmdb
                                )
                            }

                            Serie -> withContext(Dispatchers.IO) {
                                getDetailsSerieUserCase(
                                    idTmdb
                                )
                            }
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