package com.juarez.coppeldemo.domain

import com.juarez.coppeldemo.data.repositories.HeroRepository
import javax.inject.Inject

class RemoveFavoriteHeroUseCase @Inject constructor(private val repository: HeroRepository) {
    suspend operator fun invoke(heroId:Int) = repository.removeFavoriteHero(heroId)
}