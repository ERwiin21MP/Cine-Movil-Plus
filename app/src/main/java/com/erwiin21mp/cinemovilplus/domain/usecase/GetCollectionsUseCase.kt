package com.erwiin21mp.cinemovilplus.domain.usecase

import com.erwiin21mp.cinemovilplus.data.network.firebase.FirestoreManager
import com.erwiin21mp.cinemovilplus.domain.model.ContentModel
import javax.inject.Inject

class GetCollectionsUseCase @Inject constructor(private val firestoreManager: FirestoreManager) {
    suspend operator fun invoke(list: List<ContentModel>) = firestoreManager.getCollections(list)
}