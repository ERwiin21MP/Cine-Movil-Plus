package com.erwiin21mp.cinemovilplus.domain.usecase

import com.erwiin21mp.cinemovilplus.data.network.firebase.FirestoreManager
import javax.inject.Inject

class GetGendersUseCase @Inject constructor(private val firestoreManager: FirestoreManager) {
    suspend operator fun invoke() = firestoreManager.getGenders()
}