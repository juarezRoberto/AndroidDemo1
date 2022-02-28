package com.juarez.coppeldemo.domain

import com.juarez.coppeldemo.data.repositories.HeroRepository
import javax.inject.Inject

class IsFavoriteHeroUseCase @Inject constructor(private val repository: HeroRepository) {
    suspend operator fun invoke(heroId: Int): Boolean {
        val hero = repository.getFavoriteById(heroId)
        return hero == null
    }
}