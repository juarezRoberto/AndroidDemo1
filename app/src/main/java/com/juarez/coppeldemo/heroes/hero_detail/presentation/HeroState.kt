package com.juarez.coppeldemo.heroes.hero_detail.presentation

import com.juarez.coppeldemo.heroes.data.Hero

sealed class HeroState {
    object Loading : HeroState()
    object Empty : HeroState()
    data class Error(val message: String) : HeroState()
    data class Success(val data: Hero) : HeroState()
}