package com.juarez.coppeldemo.ui.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.juarez.coppeldemo.data.models.Hero
import com.juarez.coppeldemo.domain.GetFavoriteHeroesUseCase
import com.juarez.coppeldemo.domain.RemoveFavoriteHeroUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoriteHeroesViewModel @Inject constructor(
    getFavoriteHeroesUseCase: GetFavoriteHeroesUseCase,
    private val removeFavoriteHeroUseCase: RemoveFavoriteHeroUseCase,
) : ViewModel() {

    val favoriteHeroes: StateFlow<List<Hero>> =
        getFavoriteHeroesUseCase().stateIn(viewModelScope, SharingStarted.Lazily, emptyList())


    fun removeFavoriteHero(heroId: Int) = viewModelScope.launch {
        removeFavoriteHeroUseCase(heroId)
    }

}