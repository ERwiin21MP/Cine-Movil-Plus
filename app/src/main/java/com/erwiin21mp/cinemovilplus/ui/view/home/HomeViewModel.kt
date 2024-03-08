package com.erwiin21mp.cinemovilplus.ui.view.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.erwiin21mp.cinemovilplus.core.ext.logData
import com.erwiin21mp.cinemovilplus.domain.model.ContentHomeModel
import com.erwiin21mp.cinemovilplus.domain.model.PlatformModel
import com.erwiin21mp.cinemovilplus.domain.usecase.GetDetailsMovieUseCase
import com.erwiin21mp.cinemovilplus.domain.usecase.GetDetailsSerieUserCase
import com.erwiin21mp.cinemovilplus.domain.usecase.GetWatchProvidersMovieUseCase
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
    private val getWatchProvidersMovieUseCase: GetWatchProvidersMovieUseCase
) :
    ViewModel() {

    private val db = FirebaseFirestore.getInstance()
    val listOfContent = MutableLiveData<List<ContentHomeModel>>(emptyList())
    val listOfPlatforms = MutableLiveData<List<PlatformModel>>(emptyList())

    companion object {
        const val CONTENT = "content"
        const val ID = "id"
        const val ID_TMDB = "id_tmdb"
        const val TYPE_ID = "type_id"
        const val UPLOAD_DATE = "upload_date"
        const val IS_CAMERA_QUALITY = "is_camera_quality"
        const val IS_ENABLED = "is_enabled"
    }

    init {
        getContent()
    }

    private fun getContent() {
        val list = mutableListOf<ContentHomeModel>()
        val list2 = mutableListOf<PlatformModel>()
        db.collection(CONTENT).get().addOnSuccessListener { documents ->
            for (document in documents) {
                val data = document.data
                val idTmdb = data[ID_TMDB].toString()
                val typeID = data[TYPE_ID].toString().toInt()
                viewModelScope.launch {
                    val result = when (typeID) {
                        1 -> withContext(Dispatchers.IO) { getDetailsSerieUserCase(idTmdb) }
                        2 -> withContext(Dispatchers.IO) { getDetailsMovieUseCase(idTmdb) }
                        else -> null
                    }
                    list.add(
                        ContentHomeModel(
                            id = data[ID].toString(),
                            idTmdb = idTmdb,
                            isCameraQuality = data[IS_CAMERA_QUALITY].toString().toInt() != 0,
                            isEnabled = data[IS_ENABLED].toString().toInt() != 0,
                            typeID = typeID,
                            uploadDate = data[UPLOAD_DATE].toString().replace("-", "")
                                .replace(" ", "").replace(":", "").toLong(),
                            horizontalImageURL = result?.horizontalImageURL ?: "",
                            releaseDate = result?.releaseDate ?: "",
                            title = result?.title ?: "",
                            verticalImageURL = result?.verticalImageURL ?: ""
                        )
                    )
                    val result2 = withContext(Dispatchers.IO) {
                        logData(idTmdb, ID_TMDB.plus("GG"))
                        getWatchProvidersMovieUseCase(idTmdb)
                    }
                    logData(result2.toString(), "RESULT 2")
                    listOfContent.postValue(list)
                    listOfPlatforms.postValue(list2)
                    getPlatforms(idTmdb)
                }
            }
        }
    }

    private fun getPlatforms(idTmdb: String) {
        logData(idTmdb, ID_TMDB)
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) { getWatchProvidersMovieUseCase(idTmdb) }
            logData(result.toString(), "RESULT")
        }
    }
}