package com.juarez.coppeldemo.heroes.hero_detail.domain

import com.juarez.coppeldemo.heroes.data.HeroRepository
import javax.inject.Inject

class GetHeroDetailUseCase @Inject constructor(private val repository: HeroRepository) {
    operator fun invoke(heroId: Int) = repository.getHeroDetail(heroId)
}