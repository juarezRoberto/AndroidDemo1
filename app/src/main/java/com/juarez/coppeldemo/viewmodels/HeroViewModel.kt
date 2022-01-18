package com.juarez.coppeldemo.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.juarez.coppeldemo.models.Hero
import com.juarez.coppeldemo.models.Image
import com.juarez.coppeldemo.repositories.HeroRepository
import com.juarez.coppeldemo.repositories.HeroesSource
import kotlinx.coroutines.launch

class HeroViewModel(private val repository: HeroRepository) : ViewModel() {
    val heroe = MutableLiveData<Hero>()
    val loading = MutableLiveData<Boolean>()
    val error = MutableLiveData<String>()

    val heroes = Pager(config = PagingConfig(pageSize = 5), pagingSourceFactory = {
        HeroesSource(repository)
    }).flow.cachedIn(viewModelScope)

    fun getHeroDetail(heroId: Int) = viewModelScope.launch {
        loading.value = true
        repository.getHeroDetail(heroId) { isSuccess, data, message ->
            if (isSuccess) heroe.value = data
            else error.value = message
        }
        loading.value = false
    }
}