package com.juarez.coppeldemo.ui.sharedviewmodels

import androidx.lifecycle.*
import com.juarez.coppeldemo.data.models.Hero
import com.juarez.coppeldemo.domain.GetHeroDetailUseCase
import com.juarez.coppeldemo.domain.IsFavoriteHeroUseCase
import com.juarez.coppeldemo.domain.SaveFavoriteHeroUseCase
import com.juarez.coppeldemo.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
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
    private val _isFavorite = MutableLiveData<Boolean>()
    val isFavorite: LiveData<Boolean> = _isFavorite
    private val _hero = MutableLiveData<Hero>()
    val hero: LiveData<Hero> = _hero
    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading
    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error
    private val _url = MutableLiveData(state.get("hero_url") ?: "")
    val url: LiveData<String> = _url

    fun getHeroDetail(heroId: Int) {
        getHeroDetailUseCase(heroId).onEach { result ->
            when (result) {
                is Resource.Loading -> {
                    _loading.value = true
                    _isFavorite.value = false
                    _error.value = ""
                }
                is Resource.Success -> {
                    result.data.also { hero -> _hero.value = hero }
                    saveUrl(result.data.image.url)
                    getIsFavoriteHeroById(heroId)
                }
                is Resource.Error -> {
                    _error.value = result.message
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
     * update url LiveData and set value in state
     */
    private fun saveUrl(url: String?) {
        _url.value = url
        state.set("hero_url", url)
    }
}