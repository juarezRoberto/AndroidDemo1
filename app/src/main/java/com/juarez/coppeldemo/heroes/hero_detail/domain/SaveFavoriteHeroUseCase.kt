package com.juarez.coppeldemo.heroes.hero_detail.domain

import com.juarez.coppeldemo.heroes.data.Hero
import com.juarez.coppeldemo.heroes.data.HeroRepository
import javax.inject.Inject

class SaveFavoriteHeroUseCase @Inject constructor(private val repository: HeroRepository) {
    suspend operator fun invoke(hero: Hero) {
        val fav = repository.getFavoriteById(hero.id.toInt())
        if (fav == null) repository.saveFavoriteHero(hero)
    }
}