package com.juarez.coppeldemo.heroes.heroes.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.juarez.coppeldemo.heroes.data.Hero
import com.juarez.coppeldemo.heroes.heroes.domain.GetHeroesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class HeroViewModel @Inject constructor(getHeroesUseCase: GetHeroesUseCase) : ViewModel() {
    val heroes: Flow<PagingData<Hero>> = getHeroesUseCase().cachedIn(viewModelScope)
}