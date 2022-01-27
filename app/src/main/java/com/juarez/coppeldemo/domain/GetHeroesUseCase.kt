package com.juarez.coppeldemo.domain

import androidx.paging.PagingData
import com.juarez.coppeldemo.data.models.Hero
import com.juarez.coppeldemo.data.repositories.HeroRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetHeroesUseCase @Inject constructor(private val repository: HeroRepository) {
    operator fun invoke(): Flow<PagingData<Hero>> = repository.getHeroes()
}