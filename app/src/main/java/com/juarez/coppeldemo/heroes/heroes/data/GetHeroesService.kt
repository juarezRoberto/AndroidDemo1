package com.juarez.coppeldemo.heroes.heroes.data

import com.juarez.coppeldemo.api.HeroAPI
import com.juarez.coppeldemo.heroes.data.Hero
import com.juarez.coppeldemo.utils.NetworkResponse
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject

/**
 * this service emulate pagination because heroes.api does not have one
 */
class GetHeroesService @Inject constructor(private val heroAPI: HeroAPI) {
    suspend operator fun invoke(page: Int): NetworkResponse<List<Hero>> {
        var firstHero = ((page - 1) * 20) + 1
        val lastHero = ((page - 1) * 20) + 20
        if (page == 1) firstHero = 1

        val heroes = arrayListOf<Hero>()
        coroutineScope {
            val requestsDeferred = (firstHero..lastHero).map { async { heroAPI.getHero(it) } }
            val requestsResponse = requestsDeferred.awaitAll()

            requestsResponse.forEach { if (it.isSuccessful) heroes.add(it.body()!!) }
        }
        return NetworkResponse.Success(heroes)
    }
}