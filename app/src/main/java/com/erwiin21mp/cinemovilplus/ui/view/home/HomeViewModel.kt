package com.erwiin21mp.cinemovilplus.ui.view.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.erwiin21mp.cinemovilplus.domain.model.ContentHomeModel
import com.erwiin21mp.cinemovilplus.domain.usecase.GetDetailsUseCase
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val getDetailsUseCase: GetDetailsUseCase) :
    ViewModel() {

    private val db = FirebaseFirestore.getInstance()
    val listOfContent = MutableLiveData<List<ContentHomeModel>>(emptyList())

    companion object {
        const val CONTENT = "content"
        const val ID = "id"
        const val ID_TMDB = "id_tmdb"
        const val TYPE_ID = "type_id"
        const val CONTENT_URL = "content_url"
        const val UPLOAD_DATE = "upload_date"
        const val KEYWORDS = "keywords"
        const val IS_CAMERA_QUALITY = "is_camera_quality"
        const val IS_ENABLED = "is_enabled"
    }

    init {
        getContent()
    }

    private fun getContent() {
        val list = mutableListOf<ContentHomeModel>()
        db.collection(CONTENT).get().addOnSuccessListener { documents ->
            for (document in documents) {
                val data = document.data
                val idTmdb = data[ID_TMDB].toString()
                viewModelScope.launch {
                    val result = withContext(Dispatchers.IO) { getDetailsUseCase(idTmdb) }
                    list.add(
                        ContentHomeModel(
                            id = data[ID].toString(),
                            idTmdb = idTmdb,
                            isCameraQuality = data[IS_CAMERA_QUALITY].toString().toInt() != 0,
                            isEnabled = data[IS_ENABLED].toString().toInt() != 0,
                            typeID = data[TYPE_ID].toString().toInt(),
                            uploadDate = data[UPLOAD_DATE].toString().replace("-", "").replace(" ", "").replace(":", "").toLong(),
                            horizontalImageURL = result?.horizontalImageURL ?: "",
                            releaseDate = result?.releaseDate ?: "",
                            title = result?.title ?: "",
                            verticalImageURL = result?.verticalImageURL ?: ""
                        )
                    )
                    listOfContent.postValue(list)
                }
            }
        }
    }
}