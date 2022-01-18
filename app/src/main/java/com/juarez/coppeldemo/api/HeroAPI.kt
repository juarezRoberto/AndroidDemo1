package com.juarez.coppeldemo.api

import com.juarez.coppeldemo.models.Hero
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface HeroAPI {

    @GET("/api/access-token/{hero_id}")
    suspend fun getHeroes(@Path("hero_id") heroId: Int): Response<List<Hero>>

    @GET("/api/access-token/{hero_id}")
    suspend fun getHeroe(@Path("hero_id") heroId: Int): Response<Hero>
}