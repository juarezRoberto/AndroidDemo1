package com.juarez.coppeldemo.data.repositories

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.juarez.coppeldemo.api.HeroAPI
import com.juarez.coppeldemo.data.db.HeroDao
import com.juarez.coppeldemo.data.models.*
import com.juarez.coppeldemo.data.remoteDataSources.GetUserService
import com.juarez.coppeldemo.data.remoteDataSources.HeroesPagingSource
import com.juarez.coppeldemo.utils.Constants
import com.juarez.coppeldemo.utils.NetworkResponse
import com.juarez.coppeldemo.utils.Resource
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class HeroRepository @Inject constructor(
    private val heroAPI: HeroAPI,
    private val heroDao: HeroDao,
    private val getUserService: GetUserService,
) {

    fun getHeroes(): Flow<PagingData<Hero>> {
        return Pager(config = PagingConfig(pageSize = 5), pagingSourceFactory = {
            HeroesPagingSource(getUserService)
        }).flow
    }

    fun getHeroDetail(heroId: Int): Flow<Resource<Hero>> = flow {
        emit(Resource.Loading)
        try {
            coroutineScope {
                val hero = Hero()
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

                hero.id = heroId.toString()
                if (statsRes is NetworkResponse.Success) {
                    hero.name = statsRes.data!!.name!!
                    hero.powerStats = statsRes.data
                } else throw Exception(statsRes.message)

                if (imageRes is NetworkResponse.Success) {
                    hero.image = imageRes.data!!
                } else throw Exception(imageRes.message)

                if (bioRes is NetworkResponse.Success) {
                    hero.biography = bioRes.data!!
                } else throw Exception(bioRes.message)

                if (appearanceRes is NetworkResponse.Success) {
                    hero.appearance = appearanceRes.data!!
                } else throw Exception(appearanceRes.message)

                if (connectionsRes is NetworkResponse.Success) {
                    hero.connections = connectionsRes.data!!
                } else throw Exception(connectionsRes.message)

                emit(Resource.Success(hero))
            }
        } catch (e: Exception) {
            emit(Resource.Error(e.localizedMessage ?: "Unexpected error"))
        }
    }

    suspend fun getHeroImage(heroId: Int): NetworkResponse<Image> {
        var image = Image()
        return try {
            val imageRes = heroAPI.getHeroImage(heroId)
            if (!imageRes.isSuccessful) throw Exception(Constants.GENERAL_ERROR)
            imageRes.body()?.let {
                image = Image(it.url)
            }
            NetworkResponse.Success(image)
        } catch (e: HttpException) {
            NetworkResponse.Error(e.localizedMessage ?: "Unexpected error")
        } catch (e: IOException) {
            NetworkResponse.Error(Constants.CONNECTION_ERROR)
        }
    }

    suspend fun getHeroPowerStats(heroId: Int): NetworkResponse<PowerStats> {
        var powerStats = PowerStats()
        return try {
            val power = heroAPI.getHeroPowerStats(heroId)
            if (!power.isSuccessful) throw Exception(Constants.GENERAL_ERROR)
            power.body()?.let {
                powerStats = PowerStats(
                    it.name, it.intelligence, it.strength, it.speed, it.durability,
                    it.power, it.combat
                )
            }
            NetworkResponse.Success(powerStats)
        } catch (e: HttpException) {
            NetworkResponse.Error(e.localizedMessage ?: "Unexpected error")
        } catch (e: IOException) {
            NetworkResponse.Error(Constants.CONNECTION_ERROR)
        }
    }

    suspend fun getHeroBiography(heroId: Int): NetworkResponse<Biography> {
        var biography = Biography()
        return try {
            val bio = heroAPI.getHeroBiography(heroId)
            if (!bio.isSuccessful) throw Exception(Constants.GENERAL_ERROR)
            bio.body()?.let {
                biography = Biography(
                    it.name, it.fullName, it.alterEgos, it.aliases, it.placeOfBirth,
                    it.firstAppearance, it.publisher, it.alignment
                )
            }
            NetworkResponse.Success(biography)
        } catch (e: HttpException) {
            NetworkResponse.Error(e.localizedMessage ?: "Unexpected error")
        } catch (e: IOException) {
            NetworkResponse.Error(Constants.CONNECTION_ERROR)
        }
    }

    suspend fun getHeroAppearance(heroId: Int): NetworkResponse<Appearance> {
        var appearance = Appearance()
        return try {
            val bio = heroAPI.getHeroAppearance(heroId)
            if (!bio.isSuccessful) throw Exception(Constants.GENERAL_ERROR)
            bio.body()?.let {
                appearance = Appearance(
                    it.gender, it.race, it.height, it.weight, it.eyeColor, it.hairColor
                )
            }
            NetworkResponse.Success(appearance)
        } catch (e: HttpException) {
            NetworkResponse.Error(e.localizedMessage ?: "Unexpected error")
        } catch (e: IOException) {
            NetworkResponse.Error(Constants.CONNECTION_ERROR)
        }
    }

    suspend fun getHeroConnections(heroId: Int): NetworkResponse<Connections> {
        var connections = Connections()
        return try {
            val bio = heroAPI.getHeroConnections(heroId)
            if (!bio.isSuccessful) throw Exception(Constants.GENERAL_ERROR)
            bio.body()?.let {
                connections = Connections(it.groupAffiliation, it.relatives)
            }
            NetworkResponse.Success(connections)
        } catch (e: HttpException) {
            NetworkResponse.Error(e.localizedMessage ?: "Unexpected error")
        } catch (e: IOException) {
            NetworkResponse.Error(Constants.CONNECTION_ERROR)
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