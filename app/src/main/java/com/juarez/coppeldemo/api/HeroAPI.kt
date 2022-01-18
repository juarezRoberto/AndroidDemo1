package com.juarez.coppeldemo.api

import com.juarez.coppeldemo.models.*
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface HeroAPI {

    @GET("/api/access-token/{hero_id}")
    suspend fun getHero(@Path("hero_id") heroId: Int): Response<Hero>

    @GET("/api/access-token/{hero_id}/powerstats")
    suspend fun getHeroPowerStats(@Path("hero_id") heroId: Int): Response<PowerStats>

    @GET("/api/access-token/{hero_id}/biography")
    suspend fun getHeroBiography(@Path("hero_id") heroId: Int): Response<Biography>

    @GET("/api/access-token/{hero_id}/appearance")
    suspend fun getHeroAppearance(@Path("hero_id") heroId: Int): Response<Appearance>

    @GET("/api/access-token/{hero_id}/connections")
    suspend fun getHeroConnections(@Path("hero_id") heroId: Int): Response<Connections>
}