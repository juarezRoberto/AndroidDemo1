package com.juarez.coppeldemo.repositories

import com.juarez.coppeldemo.api.HeroAPI
import com.juarez.coppeldemo.models.*
import com.juarez.coppeldemo.utils.Constants
import kotlinx.coroutines.delay
import javax.inject.Inject

class HeroRepository @Inject constructor(private val heroAPI: HeroAPI) {

    /**
     * this service emulate pagination because heroes.api does not have one
     */
    suspend fun getAllHeroes(page: Int): CustomResponse<List<Hero>> {
        var customResponse: CustomResponse<List<Hero>>
        var firstHero = ((page - 1) * 20) + 1
        val lastHero = ((page - 1) * 20) + 20
        if (page == 1) firstHero = 1
        try {
            delay(1000)
            val heroes = arrayListOf<Hero>()
            for (i in firstHero..lastHero) {
                val response = heroAPI.getHero(i)
                if (response.isSuccessful) heroes.add(response.body()!!)
            }
            customResponse = CustomResponse(true, heroes, null)

        } catch (e: Exception) {
            customResponse = CustomResponse(false, null, e.message.toString())
        }

        return customResponse
    }

    suspend fun getHeroDetail(
        heroId: Int,
        callback: (isSuccess: Boolean, data: Hero?, message: String?) -> Unit
    ) {
        try {
            delay(1000)
            val powerStatsRes = getHeroPowerStats(heroId)
            val bioRes = getHeroBiography((heroId))
            val appearanceRes = getHeroAppearance(heroId)
            val connectionsRes = getHeroConnections(heroId)

            val hero = Hero(
                heroId.toString(),
                powerStatsRes.data?.name!!,
                Image(""),
                powerStatsRes.data,
                bioRes.data!!,
                appearanceRes.data!!,
                connectionsRes.data!!
            )
            callback.invoke(true, hero, null)
        } catch (e: Exception) {
            callback.invoke(false, null, e.message.toString())
        }
    }

    suspend fun getHeroPowerStats(heroId: Int): CustomResponse<PowerStats> {
        var customResponse: CustomResponse<PowerStats>
        var powerStats = PowerStats(null, null, null, null, null, null, null)
        try {
            val power = heroAPI.getHeroPowerStats(heroId)
            if (!power.isSuccessful) throw Exception(Constants.GENERAL_ERROR)
            power.body()?.let {
                powerStats = PowerStats(
                    it.name, it.intelligence, it.strength, it.speed, it.durability,
                    it.power, it.combat
                )
            }
            customResponse = CustomResponse(true, powerStats, null)
        } catch (e: Exception) {
            customResponse = CustomResponse(false, powerStats, e.message.toString())
        }
        return customResponse
    }

    suspend fun getHeroBiography(heroId: Int): CustomResponse<Biography> {
        var customResponse: CustomResponse<Biography>
        var biography = Biography(null, null, null, null, null, null, null, null)
        try {
            val bio = heroAPI.getHeroBiography(heroId)
            if (!bio.isSuccessful) throw Exception(Constants.GENERAL_ERROR)
            bio.body()?.let {
                biography = Biography(
                    it.name, it.fullName, it.alterEgos, it.aliases, it.placeOfBirth,
                    it.firstAppearance, it.publisher, it.alignment
                )
            }
            customResponse = CustomResponse(true, biography, null)

        } catch (e: Exception) {
            customResponse = CustomResponse(false, biography, e.message.toString())
        }
        return customResponse
    }

    suspend fun getHeroAppearance(heroId: Int): CustomResponse<Appearance> {
        var customResponse: CustomResponse<Appearance>
        var appearance = Appearance(null, null, null, null, null, null)
        try {
            val bio = heroAPI.getHeroAppearance(heroId)
            if (!bio.isSuccessful) throw Exception(Constants.GENERAL_ERROR)
            bio.body()?.let {
                appearance = Appearance(
                    it.gender, it.race, it.height, it.weight, it.eyeColor, it.hairColor
                )
            }
            customResponse = CustomResponse(true, appearance, null)

        } catch (e: Exception) {
            customResponse = CustomResponse(false, appearance, e.message.toString())
        }
        return customResponse
    }

    suspend fun getHeroConnections(heroId: Int): CustomResponse<Connections> {
        var customResponse: CustomResponse<Connections>
        var connections = Connections(null, null)
        try {
            val bio = heroAPI.getHeroConnections(heroId)
            if (!bio.isSuccessful) throw Exception(Constants.GENERAL_ERROR)
            bio.body()?.let {
                connections = Connections(it.groupAffiliation, it.relatives)
            }
            customResponse = CustomResponse(true, connections, null)

        } catch (e: Exception) {
            customResponse = CustomResponse(false, connections, e.message.toString())
        }
        return customResponse
    }
}