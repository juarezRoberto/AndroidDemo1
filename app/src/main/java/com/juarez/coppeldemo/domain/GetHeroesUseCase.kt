package com.juarez.coppeldemo.domain

import com.juarez.coppeldemo.data.repositories.HeroRepository
import javax.inject.Inject

class GetHeroesUseCase @Inject constructor(private val repository: HeroRepository) {
    suspend operator fun invoke(page: Int) = repository.getAllHeroes(page)
}