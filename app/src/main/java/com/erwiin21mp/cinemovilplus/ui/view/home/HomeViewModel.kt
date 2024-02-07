package com.erwiin21mp.cinemovilplus.ui.view.home

import androidx.lifecycle.ViewModel
import com.erwiin21mp.cinemovilplus.domain.model.ContentModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor() : ViewModel() {
    private var _home = MutableStateFlow<List<ContentModel>>(emptyList())
    val home:StateFlow<List<ContentModel>> = _home

    init {

    }
}