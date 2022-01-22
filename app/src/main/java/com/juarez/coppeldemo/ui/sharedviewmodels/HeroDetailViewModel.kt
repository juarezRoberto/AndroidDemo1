package com.juarez.coppeldemo.ui.sharedviewmodels

import androidx.lifecycle.*
import com.juarez.coppeldemo.data.models.Hero
import com.juarez.coppeldemo.domain.GetHeroDetailUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HeroDetailViewModel @Inject constructor(
    private val getHeroDetailUseCase: GetHeroDetailUseCase,
    private val state: SavedStateHandle
) :
    ViewModel() {
    private val _hero = MutableLiveData<Hero>()
    val hero: LiveData<Hero> = _hero
    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading
    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error
    private val _url = MutableLiveData(state.get("hero_url") ?: "")
    val url: LiveData<String> = _url

    fun getHeroDetail(heroId: Int) = viewModelScope.launch {
        _loading.value = true

        val response = getHeroDetailUseCase(heroId)
        if (response.isSuccess) response.data?.let {
            _hero.value = it
            saveUrl(it.image.url)
        }
        else _error.value = response.message!!
        _loading.value = false
    }

    /**
     * update url LiveData and set value in state
     */
    private fun saveUrl(url: String?) {
        _url.value = url
        state.set("hero_url", url)
    }
}