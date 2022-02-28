package com.juarez.coppeldemo.heroes.heroes.domain

import androidx.paging.PagingData
import com.juarez.coppeldemo.heroes.data.Hero
import com.juarez.coppeldemo.heroes.data.HeroRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetHeroesUseCase @Inject constructor(private val repository: HeroRepository) {
    operator fun invoke(): Flow<PagingData<Hero>> = repository.getHeroes()
}