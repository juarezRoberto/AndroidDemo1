package com.juarez.coppeldemo.utils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.juarez.coppeldemo.repositories.HeroRepository
import com.juarez.coppeldemo.ui.herodetailactivity.HeroDetailViewModel
import com.juarez.coppeldemo.ui.mainactivity.HeroViewModel

class HeroViewModelFactory(private val repository: HeroRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T = when (modelClass) {
        HeroViewModel::class.java -> HeroViewModel(repository)
        HeroDetailViewModel::class.java -> HeroDetailViewModel(repository)
        else -> throw IllegalArgumentException("Unknown ViewModel class")
    } as T
}