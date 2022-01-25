package com.juarez.coppeldemo.domain

import com.juarez.coppeldemo.data.models.Hero
import com.juarez.coppeldemo.data.repositories.HeroRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetFavoriteHeroesUseCase @Inject constructor(private val repository: HeroRepository) {
    operator fun invoke(): Flow<List<Hero>> {
        return repository.favoriteHeroes
    }
}