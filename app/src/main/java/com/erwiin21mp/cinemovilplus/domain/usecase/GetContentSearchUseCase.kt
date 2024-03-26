package com.erwiin21mp.cinemovilplus.domain.usecase

import com.erwiin21mp.cinemovilplus.data.network.firebase.FirestoreManager
import javax.inject.Inject

class GetContentSearchUseCase @Inject constructor(private val db: FirestoreManager) {
//    suspend operator fun invoke() = db.getContentSearch()
}