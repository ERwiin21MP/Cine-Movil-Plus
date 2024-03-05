package com.erwiin21mp.cinemovilplus.ui.view.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.erwiin21mp.cinemovilplus.core.ext.isNull
import com.erwiin21mp.cinemovilplus.core.ext.logData
import com.erwiin21mp.cinemovilplus.data.model.Content
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
    val listOfContent = MutableLiveData<List<Content>>(emptyList())

    companion object {
        const val CONTENT = "content"
        const val ID = "id"
        const val TITLE = "title"
        const val ID_TMDB = "id_tmdb"
        const val TYPE_ID = "type_id"
        const val CONTENT_URL = "content_url"
        const val UPLOAD_DATE = "upload_date"
        const val KEYWORDS = "keywords"
        const val IS_CAMERA_QUALITY = "is_camera_quality"
        const val IS_ENABLED = "is_enabled"

        const val API_KEY = "a91dbbaa0623f021f2c4220e3dd7a70a"
    }

    init {
        getContent()
    }

    fun getDetail() {
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) {
                getDetailsUseCase(
                    apiKey = API_KEY,
                    id = "122906",
                    language = "es-MX"
                )
            }
            if (!result.isNull()) {
                logData(result.toString(), "result")
            } else {
                logData("Ha error")
            }
        }
    }

    private fun getContent() {
        val list = mutableListOf<Content>()
        db.collection(CONTENT).get().addOnSuccessListener { documents ->
            for (document in documents) {
                val data = document.data
                list.add(
                    Content(
                        id = data[ID].toString().toInt(),
                        idTmdb = data[ID_TMDB].toString().toInt(),
                        contentUrl = data[CONTENT_URL].toString(),
                        isCameraQuality = data[IS_CAMERA_QUALITY].toString().toInt() != 0,
                        isEnabled = data[IS_ENABLED].toString().toInt() != 0,
                        keywords = data[KEYWORDS].toString(),
                        typeID = data[TYPE_ID].toString().toInt(),
                        uploadDate = data[UPLOAD_DATE].toString().replace("-", "").replace(":", "")
                            .replace(" ", "").toLong()
                    )
                )
            }
            listOfContent.postValue(list)
        }
    }
}