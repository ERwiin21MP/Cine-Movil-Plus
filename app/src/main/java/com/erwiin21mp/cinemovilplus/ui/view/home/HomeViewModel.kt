package com.erwiin21mp.cinemovilplus.ui.view.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.erwiin21mp.cinemovilplus.core.ext.isNotNull
import com.erwiin21mp.cinemovilplus.core.ext.logList
import com.erwiin21mp.cinemovilplus.domain.model.CollectionModel
import com.erwiin21mp.cinemovilplus.domain.model.ContentHomeModel
import com.erwiin21mp.cinemovilplus.domain.model.ContentModel
import com.erwiin21mp.cinemovilplus.domain.model.FlatrateModel
import com.erwiin21mp.cinemovilplus.domain.model.GenderModel
import com.erwiin21mp.cinemovilplus.domain.usecase.GetCollectionDetailsUseCase
import com.erwiin21mp.cinemovilplus.domain.usecase.GetContentHomeUseCase
import com.erwiin21mp.cinemovilplus.domain.usecase.GetGendersUseCase
import com.erwiin21mp.cinemovilplus.domain.usecase.GetPlatformsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getContentHomeUseCase: GetContentHomeUseCase,
    private val getPlatformsUseCase: GetPlatformsUseCase,

    private val getCollectionDetailsUseCase: GetCollectionDetailsUseCase,
    private val getGendersUseCase: GetGendersUseCase,
) : ViewModel() {
    val listOfContentFeatured = MutableLiveData<List<ContentModel>>(emptyList())
    val listOfPlatforms = MutableLiveData<List<FlatrateModel>>(emptyList())

    val listOfGenders = MutableLiveData<List<GenderModel>>(emptyList())
    val listAllContent = MutableLiveData<List<ContentHomeModel>>(emptyList())
    val listCurrentYear = MutableLiveData<List<ContentHomeModel>>(emptyList())
    val listCineMovilPlusNews = MutableLiveData<List<ContentHomeModel>>(emptyList())
    val listOfCollections = MutableLiveData<List<CollectionModel>>(emptyList())

    init {
        getContent()
        getGenders()
    }

    private fun getGenders() {
        viewModelScope.launch {
            val list = withContext(Dispatchers.IO) { getGendersUseCase() }
            list.shuffled()
            listOfGenders.postValue(list)
        }
    }

    private fun getContent() {
        viewModelScope.launch {
            val listOfContent = withContext(Dispatchers.IO) { getContentHomeUseCase() }
            logList(listOfContent)
            getContentFeatured(listOfContent)
            getPlatforms(listOfContent)
        }
    }

    private fun getPlatforms(list: List<ContentModel>) {
        viewModelScope.launch {
            val listOfPlatformsAux =
                withContext(Dispatchers.IO) { getPlatformsUseCase(list) }//.sortedBy { it.displayPriority }.distinct()
            logList(listOfPlatformsAux, "listOfPlatforms")
            listOfPlatforms.postValue(listOfPlatformsAux)
        }
    }

    private fun getContentFeatured(list: List<ContentModel>) {
        listOfContentFeatured.postValue(list)
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
