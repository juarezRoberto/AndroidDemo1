package com.juarez.coppeldemo.ui.sharedviewmodels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.juarez.coppeldemo.data.models.Hero
import com.juarez.coppeldemo.domain.GetHeroDetailUseCase
import com.juarez.coppeldemo.domain.IsFavoriteHeroUseCase
import com.juarez.coppeldemo.domain.SaveFavoriteHeroUseCase
import com.juarez.coppeldemo.ui.herodetail.HeroState
import com.juarez.coppeldemo.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HeroDetailViewModel @Inject constructor(
    private val getHeroDetailUseCase: GetHeroDetailUseCase,
    private val saveFavoriteHeroUseCase: SaveFavoriteHeroUseCase,
    private val isFavoriteHeroUseCase: IsFavoriteHeroUseCase,
    private val state: SavedStateHandle,
) :
    ViewModel() {
    private val _isFavorite = MutableStateFlow(false)
    val isFavorite: StateFlow<Boolean> = _isFavorite
    private val _hero = MutableStateFlow(Hero())
    val hero: StateFlow<Hero> = _hero
    private val _url = MutableStateFlow(state.get("hero_url") ?: "")
    val url: StateFlow<String> = _url

    private val _heroState = MutableStateFlow<HeroState>(HeroState.Empty)
    val heroState: StateFlow<HeroState> = _heroState

    fun getHeroDetail(heroId: Int) {
        getHeroDetailUseCase(heroId).onEach { result ->
            when (result) {
                is Resource.Loading -> {
                    _isFavorite.value = false
                    _heroState.value = HeroState.Error("")
                    _heroState.value = HeroState.Loading
                }
                is Resource.Success -> {
                    _heroState.value = HeroState.Success(result.data)
                    saveUrl(result.data.image.url ?: "")
                    getIsFavoriteHeroById(heroId)
                }
                is Resource.Error -> {
                    _heroState.value = HeroState.Error(result.message)
                }
            }
        }.launchIn(viewModelScope)
    }

    fun saveFavoriteHero(hero: Hero) = viewModelScope.launch {
        saveFavoriteHeroUseCase(hero)
        getIsFavoriteHeroById(hero.id.toInt())
    }

    private suspend fun getIsFavoriteHeroById(heroId: Int) {
        isFavoriteHeroUseCase(heroId).also { _isFavorite.value = it }
    }

    /**
     * update url and set value in state
     */
    private fun saveUrl(url: String) {
        _url.value = url
        state.set("hero_url", url)
    }
}