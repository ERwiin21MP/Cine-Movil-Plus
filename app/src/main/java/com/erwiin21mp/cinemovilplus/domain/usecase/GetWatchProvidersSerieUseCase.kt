package com.erwiin21mp.cinemovilplus.domain.usecase

import com.erwiin21mp.cinemovilplus.domain.Repository
import javax.inject.Inject

class GetWatchProvidersSerieUseCase @Inject constructor(private val repository: Repository) {
    suspend operator fun invoke(id: String) = repository.getWatchProvidersSerie(id)
}