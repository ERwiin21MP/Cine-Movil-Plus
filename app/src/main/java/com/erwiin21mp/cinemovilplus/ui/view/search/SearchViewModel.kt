package com.erwiin21mp.cinemovilplus.ui.view.search

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.erwiin21mp.cinemovilplus.domain.model.ContentModel
import com.erwiin21mp.cinemovilplus.domain.usecase.GetContentSearchUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(private val getContentSearchUseCase: GetContentSearchUseCase) : ViewModel() {

    val listOfContent = MutableLiveData<List<ContentModel>>(emptyList())

    init {
        getContent()
    }

    private fun getContent() {
        viewModelScope.launch {
            val listOfContentAux = withContext(Dispatchers.IO) { getContentSearchUseCase() }
            listOfContent.postValue(listOfContentAux)
        }
    }
}