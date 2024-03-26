package com.erwiin21mp.cinemovilplus.ui.view.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.erwiin21mp.cinemovilplus.domain.model.CollectionModel
import com.erwiin21mp.cinemovilplus.domain.model.ContentModel
import com.erwiin21mp.cinemovilplus.domain.model.FlatrateModel
import com.erwiin21mp.cinemovilplus.domain.model.GenderModel
import com.erwiin21mp.cinemovilplus.domain.usecase.GetCollectionsUseCase
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
    private val getGendersUseCase: GetGendersUseCase,
    private val getCollectionsUseCase: GetCollectionsUseCase
) : ViewModel() {
    val listOfContentFeatured = MutableLiveData<List<ContentModel>>(emptyList())
    val listOfPlatforms = MutableLiveData<List<FlatrateModel>>(emptyList())

    val listOfGenders = MutableLiveData<List<GenderModel>>(emptyList())
    val listAllContent = MutableLiveData<List<ContentModel>>(emptyList())
    val listCurrentYear = MutableLiveData<List<ContentModel>>(emptyList())
    val listCineMovilPlusNews = MutableLiveData<List<ContentModel>>(emptyList())
    val listOfCollections = MutableLiveData<List<CollectionModel>>(emptyList())

    init {
        getContent()
        getGenders()
    }

    private fun getContent() {
        viewModelScope.launch {
            val listOfContent = withContext(Dispatchers.IO) { getContentHomeUseCase() }
            getContentFeatured(listOfContent)
            getPlatforms(listOfContent)
            getAllContent(listOfContent)
            getContentCurrentYear(listOfContent)
            getCineMovilPlusNews(listOfContent)
            getCollections(listOfContent)
        }
    }

    private fun getContentFeatured(list: List<ContentModel>) {
        listOfContentFeatured.postValue(list)
    }

    private fun getAllContent(list: List<ContentModel>) {
        listAllContent.postValue(list)
    }

    private fun getGenders() {
        viewModelScope.launch {
            val list = withContext(Dispatchers.IO) { getGendersUseCase() }
            listOfGenders.postValue(list)
        }
    }

    private fun getPlatforms(list: List<ContentModel>) {
        viewModelScope.launch {
            val listOfPlatformsAux = withContext(Dispatchers.IO) { getPlatformsUseCase(list) }
            listOfPlatforms.postValue(listOfPlatformsAux)
        }
    }

    private fun getCollections(list: List<ContentModel>) {
        viewModelScope.launch {
            val listOfCollectionsAux = withContext(Dispatchers.IO) { getCollectionsUseCase(list) }
            listOfCollections.postValue(listOfCollectionsAux)
        }
    }

    private fun getCineMovilPlusNews(list: List<ContentModel>) {
        listCineMovilPlusNews.postValue(list.sortedByDescending { it.uploadDate })
    }

    private fun getContentCurrentYear(list: List<ContentModel>) {
        val currentYear = list.map { it.releaseDate?.substring(0, 4)?.toShort() ?: 2024 }.maxOf { it }.toString()
        listCurrentYear.postValue(list.filter { it.releaseDate?.substring(0, 4) == currentYear })
    }
}
