package com.juarez.coppeldemo.heroes.favorites.domain

import com.juarez.coppeldemo.heroes.data.Hero
import com.juarez.coppeldemo.heroes.data.HeroRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetFavoriteHeroesUseCase @Inject constructor(private val repository: HeroRepository) {
    operator fun invoke(): Flow<List<Hero>> {
        return repository.favoriteHeroes
    }
}