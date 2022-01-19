package com.juarez.coppeldemo.ui.heroes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.juarez.coppeldemo.repositories.HeroRepository
import com.juarez.coppeldemo.repositories.HeroesSource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HeroViewModel @Inject constructor(private val repository: HeroRepository) : ViewModel() {

    val heroes = Pager(config = PagingConfig(pageSize = 5), pagingSourceFactory = {
        HeroesSource(repository)
    }).flow.cachedIn(viewModelScope)

}