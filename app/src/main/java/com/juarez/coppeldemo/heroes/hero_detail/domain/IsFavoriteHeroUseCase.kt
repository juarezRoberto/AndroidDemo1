package com.juarez.coppeldemo.heroes.hero_detail.domain

import com.juarez.coppeldemo.heroes.data.HeroRepository
import javax.inject.Inject

class IsFavoriteHeroUseCase @Inject constructor(private val repository: HeroRepository) {
    suspend operator fun invoke(heroId: Int): Boolean {
        val hero = repository.getFavoriteById(heroId)
        return hero == null
    }
}