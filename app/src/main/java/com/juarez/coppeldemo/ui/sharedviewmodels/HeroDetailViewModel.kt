package com.juarez.coppeldemo.ui.sharedviewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.juarez.coppeldemo.data.models.Hero
import com.juarez.coppeldemo.domain.GetHeroDetailUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HeroDetailViewModel @Inject constructor(private val getHeroDetailUseCase: GetHeroDetailUseCase) :
    ViewModel() {
    private val _hero = MutableLiveData<Hero>()
    val hero: LiveData<Hero> = _hero
    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading
    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    fun getHeroDetail(heroId: Int) = viewModelScope.launch {
        _loading.value = true

        val response = getHeroDetailUseCase(heroId)
        if (response.isSuccess) _hero.value = response.data!!
        else _error.value = response.message!!

        _loading.value = false
    }
}