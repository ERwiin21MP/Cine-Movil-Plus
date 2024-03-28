package com.erwiin21mp.cinemovilplus.domain.usecase

import com.erwiin21mp.cinemovilplus.data.network.firebase.FirestoreManager
import javax.inject.Inject

class GetLogAppOpenUseCase @Inject constructor(private val db: FirestoreManager) {
    suspend operator fun invoke(uid: String) = db.fetchLogAppOpen(uid)
}