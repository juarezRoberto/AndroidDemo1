package com.juarez.coppeldemo.ui.herodetail

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.juarez.coppeldemo.models.Hero
import com.juarez.coppeldemo.repositories.HeroRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HeroDetailViewModel @Inject constructor(private val repository: HeroRepository) :
    ViewModel() {
    val hero = MutableLiveData<Hero>()
    val loading = MutableLiveData<Boolean>()
    val error = MutableLiveData<String>()

    fun getHeroDetail(heroId: Int) = viewModelScope.launch {
        loading.value = true
        repository.getHeroDetail(heroId) { isSuccess, data, message ->
            if (isSuccess) hero.value = data
            else error.value = message
        }
        loading.value = false
    }
}