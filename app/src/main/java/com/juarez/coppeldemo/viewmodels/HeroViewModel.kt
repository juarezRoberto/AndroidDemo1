package com.juarez.coppeldemo.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.juarez.coppeldemo.models.Hero
import com.juarez.coppeldemo.repositories.HeroRepository
import kotlinx.coroutines.launch

class HeroViewModel(private val repository: HeroRepository) : ViewModel() {

    val heroes = MutableLiveData<List<Hero>>(arrayListOf())
    val loading = MutableLiveData<Boolean>()
    val error = MutableLiveData<String>()

    fun getHeroes(page: Int) = viewModelScope.launch {
        loading.value = true
        repository.getHeroes(page) { isSuccess, data, message ->
//            val all = ArrayList<Hero>()
//            all.addAll(data!!)
//            all.addAll(heroes.value!!)
//            for (hero in all)  {
//                Log.d("hero", hero.name)
//            }
            if (isSuccess) heroes.value = data
            else error.value = message
        }
        loading.value = false
    }
}