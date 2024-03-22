package com.erwiin21mp.cinemovilplus.data.network.firebase

import com.erwiin21mp.cinemovilplus.domain.model.GenderModel
import com.erwiin21mp.cinemovilplus.ui.view.home.HomeViewModel.Companion.GENDER
import com.erwiin21mp.cinemovilplus.ui.view.home.HomeViewModel.Companion.GENDERS
import com.erwiin21mp.cinemovilplus.ui.view.home.HomeViewModel.Companion.ID
import com.erwiin21mp.cinemovilplus.ui.view.home.HomeViewModel.Companion.IMAGE_URL
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirestoreManager @Inject constructor() {

    private val db = FirebaseFirestore.getInstance()

    suspend fun getGenders(): List<GenderModel> {
        return db.collection(GENDERS).get().await().documents.map { document ->
            val data = document.data!!
            GenderModel(id = data[ID].toString().toInt(), gender = data[GENDER].toString(), imageURL = data[IMAGE_URL].toString())
        }
    }
}