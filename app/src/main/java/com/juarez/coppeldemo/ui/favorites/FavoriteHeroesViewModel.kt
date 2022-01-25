package com.juarez.coppeldemo.ui.favorites

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.juarez.coppeldemo.data.models.Hero
import com.juarez.coppeldemo.domain.GetFavoriteHeroesUseCase
import com.juarez.coppeldemo.domain.RemoveFavoriteHeroUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoriteHeroesViewModel @Inject constructor(
    private val getFavoriteHeroesUseCase: GetFavoriteHeroesUseCase,
    private val removeFavoriteHeroUseCase: RemoveFavoriteHeroUseCase
) : ViewModel() {

    val favoriteHeroes: LiveData<List<Hero>> = getFavoriteHeroesUseCase().asLiveData()

    fun removeFavoriteHero(heroId: Int) = viewModelScope.launch {
        removeFavoriteHeroUseCase(heroId)
    }

}