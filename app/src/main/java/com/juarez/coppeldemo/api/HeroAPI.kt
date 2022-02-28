package com.juarez.coppeldemo.api

import com.juarez.coppeldemo.heroes.data.*
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface HeroAPI {

    @GET("{hero_id}")
    suspend fun getHero(@Path("hero_id") heroId: Int): Response<Hero>

    @GET("{hero_id}/powerstats")
    suspend fun getHeroPowerStats(@Path("hero_id") heroId: Int): Response<PowerStats>

    @GET("{hero_id}/biography")
    suspend fun getHeroBiography(@Path("hero_id") heroId: Int): Response<Biography>

    @GET("{hero_id}/appearance")
    suspend fun getHeroAppearance(@Path("hero_id") heroId: Int): Response<Appearance>

    @GET("{hero_id}/connections")
    suspend fun getHeroConnections(@Path("hero_id") heroId: Int): Response<Connections>

    @GET("{hero_id}/image")
    suspend fun getHeroImage(@Path("hero_id") heroId: Int): Response<Image>
}