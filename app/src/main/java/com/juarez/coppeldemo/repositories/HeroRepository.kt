package com.juarez.coppeldemo.repositories

import com.juarez.coppeldemo.api.WebService
import com.juarez.coppeldemo.models.Hero
import com.juarez.coppeldemo.utils.Constants
import kotlinx.coroutines.delay

class HeroRepository {

    suspend fun getHeroes(
        page: Int,
        callback: (isSuccess: Boolean, data: List<Hero>?, message: String?) -> Unit
    ) {
        var firstHero = (page - 1) * 20
        val lastHero = firstHero + 20
        if(page == 1) firstHero = 1
        try {
            delay(1000)
            val heroes = arrayListOf<Hero>()
            for (i in firstHero..lastHero) {
                val response = WebService.service().getHeroe(i)
                if (response.isSuccessful) heroes.add(response.body()!!)
                else callback.invoke(false, null, Constants.GENERAL_ERROR)
            }
            callback.invoke(true, heroes, null)

        } catch (e: Exception) {
            callback.invoke(false, null, e.message.toString())
        }
    }
}