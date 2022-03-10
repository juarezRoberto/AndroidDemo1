package com.juarez.coppeldemo.heroes.hero_detail.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.juarez.coppeldemo.heroes.data.Hero
import com.juarez.coppeldemo.heroes.hero_detail.domain.GetHeroDetailUseCase
import com.juarez.coppeldemo.heroes.hero_detail.domain.IsFavoriteHeroUseCase
import com.juarez.coppeldemo.heroes.hero_detail.domain.SaveFavoriteHeroUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
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
        _isFavorite.value = false
        _heroState.value = HeroState.Error("")
        _heroState.value = HeroState.Loading
        getHeroDetailUseCase(heroId).onEach { hero ->
            _heroState.value = HeroState.Success(hero)
            saveUrl(hero.image.url ?: "")
            getIsFavoriteHeroById(heroId)
        }.catch { e ->
            _heroState.value = HeroState.Error(e.localizedMessage ?: "Unexpected Error")
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