package com.juarez.coppeldemo.ui.herodetail

import com.juarez.coppeldemo.data.models.Hero

sealed class HeroState {
    object Loading : HeroState()
    object Empty : HeroState()
    data class Error(val message: String) : HeroState()
    data class Success(val data: Hero) : HeroState()
}