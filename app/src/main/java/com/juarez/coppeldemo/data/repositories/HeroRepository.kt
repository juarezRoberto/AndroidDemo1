package com.juarez.coppeldemo.data.repositories

import com.juarez.coppeldemo.api.HeroAPI
import com.juarez.coppeldemo.data.db.HeroDao
import com.juarez.coppeldemo.data.models.*
import com.juarez.coppeldemo.utils.Constants
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class HeroRepository @Inject constructor(
    private val heroAPI: HeroAPI,
    private val heroDao: HeroDao
) {

    /**
     * this service emulate pagination because heroes.api does not have one
     */
    suspend fun getAllHeroes(page: Int): CustomResponse<List<Hero>> {
        var customResponse: CustomResponse<List<Hero>>
        var firstHero = ((page - 1) * 20) + 1
        val lastHero = ((page - 1) * 20) + 20
        if (page == 1) firstHero = 1
        try {
            val heroes = arrayListOf<Hero>()
            coroutineScope {
                val requestsDeferred = (firstHero..lastHero).map { async { heroAPI.getHero(it) } }
                val requestsResponse = requestsDeferred.awaitAll()

                requestsResponse.forEach { if (it.isSuccessful) heroes.add(it.body()!!) }
            }
            customResponse = CustomResponse(isSuccess = true, data = heroes)

        } catch (e: Exception) {
            customResponse = CustomResponse(message = e.message.toString())
        }
        return customResponse
    }

    suspend fun getHeroDetail(heroId: Int): CustomResponse<Hero> {
        var customResponse: CustomResponse<Hero>
        try {
            coroutineScope {
                val statsDeferred = async { getHeroPowerStats(heroId) }
                val bioDeferred = async { getHeroBiography(heroId) }
                val appearanceDeferred = async { getHeroAppearance(heroId) }
                val connectionsDeferred = async { getHeroConnections(heroId) }
                val imageDeferred = async { getHeroImage(heroId) }
                val statsRes = statsDeferred.await()
                val bioRes = bioDeferred.await()
                val appearanceRes = appearanceDeferred.await()
                val connectionsRes = connectionsDeferred.await()
                val imageRes = imageDeferred.await()

                val hero = Hero(
                    heroId.toString(),
                    statsRes.data?.name!!,
                    imageRes.data!!,
                    statsRes.data,
                    bioRes.data!!,
                    appearanceRes.data!!,
                    connectionsRes.data!!
                )
                customResponse = CustomResponse(isSuccess = true, data = hero)
            }
        } catch (e: Exception) {
            customResponse = CustomResponse(message = e.message.toString())
        }
        return customResponse
    }

    suspend fun getHeroImage(heroId: Int): CustomResponse<Image> {
        var image = Image()
        try {
            val imageRes = heroAPI.getHeroImage(heroId)
            if (!imageRes.isSuccessful) throw Exception(Constants.GENERAL_ERROR)
            imageRes.body()?.let {
                image = Image(it.url)
            }
            return CustomResponse(isSuccess = true, data = image)
        } catch (e: Exception) {
            return CustomResponse(data = image, message = e.message.toString())
        }
    }

    suspend fun getHeroPowerStats(heroId: Int): CustomResponse<PowerStats> {
        var powerStats = PowerStats()
        try {
            val power = heroAPI.getHeroPowerStats(heroId)
            if (!power.isSuccessful) throw Exception(Constants.GENERAL_ERROR)
            power.body()?.let {
                powerStats = PowerStats(
                    it.name, it.intelligence, it.strength, it.speed, it.durability,
                    it.power, it.combat
                )
            }
            return CustomResponse(isSuccess = true, data = powerStats)
        } catch (e: Exception) {
            return CustomResponse(data = powerStats, message = e.message.toString())
        }
    }

    suspend fun getHeroBiography(heroId: Int): CustomResponse<Biography> {
        var biography = Biography()
        try {
            val bio = heroAPI.getHeroBiography(heroId)
            if (!bio.isSuccessful) throw Exception(Constants.GENERAL_ERROR)
            bio.body()?.let {
                biography = Biography(
                    it.name, it.fullName, it.alterEgos, it.aliases, it.placeOfBirth,
                    it.firstAppearance, it.publisher, it.alignment
                )
            }
            return CustomResponse(isSuccess = true, data = biography)

        } catch (e: Exception) {
            return CustomResponse(data = biography, message = e.message.toString())
        }
    }

    suspend fun getHeroAppearance(heroId: Int): CustomResponse<Appearance> {
        var appearance = Appearance()
        try {
            val bio = heroAPI.getHeroAppearance(heroId)
            if (!bio.isSuccessful) throw Exception(Constants.GENERAL_ERROR)
            bio.body()?.let {
                appearance = Appearance(
                    it.gender, it.race, it.height, it.weight, it.eyeColor, it.hairColor
                )
            }
            return CustomResponse(isSuccess = true, data = appearance)

        } catch (e: Exception) {
            return CustomResponse(data = appearance, message = e.message.toString())
        }
    }

    suspend fun getHeroConnections(heroId: Int): CustomResponse<Connections> {
        var connections = Connections()
        try {
            val bio = heroAPI.getHeroConnections(heroId)
            if (!bio.isSuccessful) throw Exception(Constants.GENERAL_ERROR)
            bio.body()?.let {
                connections = Connections(it.groupAffiliation, it.relatives)
            }
            return CustomResponse(isSuccess = true, data = connections)

        } catch (e: Exception) {
            return CustomResponse(data = connections, message = e.message.toString())
        }
    }

    suspend fun saveFavoriteHero(hero: HeroEntity) {
        val fav = getFavoriteById(hero.id)
        if (fav == null) heroDao.insert(hero)
    }

    suspend fun getFavoriteById(heroId: Int) = heroDao.getFavoriteHeroById(heroId)

    suspend fun isFavoriteHero(heroId: Int): Boolean {
        val hero = getFavoriteById(heroId)
        return hero == null
    }

    suspend fun removeFavoriteHero(heroId: Int) = heroDao.removeFavoriteHero(heroId)

    val favoriteHeroes = heroDao.getAllHeroes().map { heroEntities ->
        heroEntities.map { heroEntity -> heroEntity.toModel() }
    }
}