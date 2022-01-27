package com.juarez.coppeldemo.domain

import com.juarez.coppeldemo.data.repositories.HeroRepository
import javax.inject.Inject

class GetHeroDetailUseCase @Inject constructor(private val repository: HeroRepository) {
    operator fun invoke(heroId: Int) = repository.getHeroDetail(heroId)
}