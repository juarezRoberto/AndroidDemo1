package com.juarez.coppeldemo.domain

import com.juarez.coppeldemo.data.models.Hero
import com.juarez.coppeldemo.data.repositories.HeroRepository
import javax.inject.Inject

class SaveFavoriteHeroUseCase @Inject constructor(private val repository: HeroRepository) {
    suspend operator fun invoke(hero: Hero) {
        val fav = repository.getFavoriteById(hero.id.toInt())
        if (fav == null) repository.saveFavoriteHero(hero)
    }
}