package com.juarez.coppeldemo.domain

import com.juarez.coppeldemo.data.repositories.HeroRepository
import javax.inject.Inject

class IsFavoriteHeroUseCase @Inject constructor(private val repository: HeroRepository) {
    suspend operator fun invoke(heroId: Int) = repository.isFavoriteHero(heroId)
}