package com.juarez.coppeldemo.ui.herodetail

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.juarez.coppeldemo.domain.GetHeroDetailUseCase
import com.juarez.coppeldemo.data.models.Hero
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HeroDetailViewModel @Inject constructor(private val getHeroDetailUseCase: GetHeroDetailUseCase) :
    ViewModel() {
    val hero = MutableLiveData<Hero>()
    val loading = MutableLiveData<Boolean>()
    val error = MutableLiveData<String>()

    fun getHeroDetail(heroId: Int) = viewModelScope.launch {
        loading.value = true

        val response = getHeroDetailUseCase(heroId)
        if (response.isSuccess) hero.value = response.data!!
        else error.value = response.message!!

        loading.value = false
    }
}