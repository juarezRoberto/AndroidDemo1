package com.juarez.coppeldemo.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.juarez.coppeldemo.repositories.HeroRepository

class HeroViewModelFactory(private val repository: HeroRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return HeroViewModel(repository) as T
    }
}