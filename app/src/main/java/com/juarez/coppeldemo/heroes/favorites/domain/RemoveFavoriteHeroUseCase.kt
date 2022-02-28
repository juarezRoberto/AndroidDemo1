package com.juarez.coppeldemo.heroes.favorites.domain

import com.juarez.coppeldemo.heroes.data.HeroRepository
import javax.inject.Inject

class RemoveFavoriteHeroUseCase @Inject constructor(private val repository: HeroRepository) {
    suspend operator fun invoke(heroId: Int) = repository.removeFavoriteHero(heroId)
}