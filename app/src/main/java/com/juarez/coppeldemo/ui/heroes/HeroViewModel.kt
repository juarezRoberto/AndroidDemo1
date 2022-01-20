package com.juarez.coppeldemo.ui.heroes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.juarez.coppeldemo.domain.GetHeroesUseCase
import com.juarez.coppeldemo.data.repositories.HeroesSource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HeroViewModel @Inject constructor(private val getHeroesUseCase: GetHeroesUseCase) : ViewModel() {

    val heroes = Pager(config = PagingConfig(pageSize = 5), pagingSourceFactory = {
        HeroesSource(getHeroesUseCase)
    }).flow.cachedIn(viewModelScope)

}