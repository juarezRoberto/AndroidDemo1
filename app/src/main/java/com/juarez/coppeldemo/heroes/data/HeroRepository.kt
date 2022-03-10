package com.juarez.coppeldemo.heroes.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.juarez.coppeldemo.api.HeroAPI
import com.juarez.coppeldemo.db.HeroDao
import com.juarez.coppeldemo.heroes.heroes.data.GetHeroesService
import com.juarez.coppeldemo.heroes.heroes.data.HeroesPagingSource
import com.juarez.coppeldemo.utils.Constants
import com.juarez.coppeldemo.utils.NetworkResponse
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import retrofit2.HttpException
import java.io.IOException

interface HeroRepository {
    val favoriteHeroes: Flow<List<Hero>>
    fun getHeroes(): Flow<PagingData<Hero>>
    fun getHeroDetail(heroId: Int): Flow<Hero>
    suspend fun saveFavoriteHero(hero: Hero)
    suspend fun getFavoriteById(heroId: Int): HeroEntity?
    suspend fun removeFavoriteHero(heroId: Int)
}

class HeroRepositoryImp(
    private val heroAPI: HeroAPI,
    private val heroDao: HeroDao,
    private val getHeroesService: GetHeroesService,
) : HeroRepository {

    override fun getHeroes(): Flow<PagingData<Hero>> {
        return Pager(config = PagingConfig(pageSize = 5), pagingSourceFactory = {
            HeroesPagingSource(getHeroesService)
        }).flow
    }

    override fun getHeroDetail(heroId: Int): Flow<Hero> = flow {
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

            emit(hero)
        }
    }

    private suspend fun getHeroImage(heroId: Int): NetworkResponse<Image> {
        var image = Image()
        return try {
            val imageRes = heroAPI.getHeroImage(heroId)
            if (!imageRes.isSuccessful) throw Exception(Constants.GENERAL_ERROR)
            imageRes.body()?.let {
                image = Image(it.url)
            }
            NetworkResponse.Success(image)
        } catch (e: HttpException) {
            NetworkResponse.Error(e.localizedMessage ?: Constants.UNEXPECTED_ERROR)
        } catch (e: IOException) {
            NetworkResponse.Error(Constants.CONNECTION_ERROR)
        }
    }

    private suspend fun getHeroPowerStats(heroId: Int): NetworkResponse<PowerStats> {
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
            NetworkResponse.Error(e.localizedMessage ?: Constants.UNEXPECTED_ERROR)
        } catch (e: IOException) {
            NetworkResponse.Error(Constants.CONNECTION_ERROR)
        }
    }

    private suspend fun getHeroBiography(heroId: Int): NetworkResponse<Biography> {
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
            NetworkResponse.Error(e.localizedMessage ?: Constants.UNEXPECTED_ERROR)
        } catch (e: IOException) {
            NetworkResponse.Error(Constants.CONNECTION_ERROR)
        }
    }

    private suspend fun getHeroAppearance(heroId: Int): NetworkResponse<Appearance> {
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
            NetworkResponse.Error(e.localizedMessage ?: Constants.UNEXPECTED_ERROR)
        } catch (e: IOException) {
            NetworkResponse.Error(Constants.CONNECTION_ERROR)
        }
    }

    private suspend fun getHeroConnections(heroId: Int): NetworkResponse<Connections> {
        var connections = Connections()
        return try {
            val bio = heroAPI.getHeroConnections(heroId)
            if (!bio.isSuccessful) throw Exception(Constants.GENERAL_ERROR)
            bio.body()?.let {
                connections = Connections(it.groupAffiliation, it.relatives)
            }
            NetworkResponse.Success(connections)
        } catch (e: HttpException) {
            NetworkResponse.Error(e.localizedMessage ?: Constants.UNEXPECTED_ERROR)
        } catch (e: IOException) {
            NetworkResponse.Error(Constants.CONNECTION_ERROR)
        }
    }

    override suspend fun saveFavoriteHero(hero: Hero) = heroDao.insert(hero.toEntity())

    override suspend fun getFavoriteById(heroId: Int) = heroDao.getFavoriteHeroById(heroId)

    override suspend fun removeFavoriteHero(heroId: Int) = heroDao.removeFavoriteHero(heroId)

    override val favoriteHeroes: Flow<List<Hero>> = heroDao.getAllHeroes().map { heroEntities ->
        heroEntities.map { heroEntity -> heroEntity.toModel() }
    }
}