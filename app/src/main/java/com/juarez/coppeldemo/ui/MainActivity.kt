package com.juarez.coppeldemo.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.juarez.coppeldemo.adapters.HeroesAdapter
import com.juarez.coppeldemo.databinding.ActivityMainBinding
import com.juarez.coppeldemo.models.Hero
import com.juarez.coppeldemo.repositories.HeroRepository
import com.juarez.coppeldemo.viewmodels.HeroViewModel
import com.juarez.coppeldemo.viewmodels.HeroViewModelFactory

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: HeroViewModel
    private val heroesAdapter = HeroesAdapter(arrayListOf()) { onItemClicked(it) }
    private var isLoading = false
    private lateinit var layoutManager: LinearLayoutManager
    private var heroesSize = 0
    private var page = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val provider = HeroViewModelFactory(HeroRepository());
        viewModel = ViewModelProvider(this, provider).get(HeroViewModel::class.java)
        binding.recyclerHeroes.apply {
            layoutManager = GridLayoutManager(context, 2)
            adapter = heroesAdapter
        }
//        binding.recyclerHeroes.addOnScrollListener(object: RecyclerView.OnScrollListener() {
//            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
//                super.onScrolled(recyclerView, dx, dy)
//                if(!isLoading) {
//                    if(layoutManager.findLastCompletelyVisibleItemPosition() == heroesSize - 1) {
//                        page += 1
//                        viewModel.getHeroes(page)
//                    }
//                }
//            }
//        })
        viewModel.getHeroes(page)
        viewModel.heroes.observe(this, {
            isLoading = false
            heroesSize = it.size
            heroesAdapter.updateData(it)
        })
        viewModel.loading.observe(this, {
            binding.progressBarHeroes.isVisible = it
        })

        viewModel.error.observe(this, {

        })
    }

    private fun onItemClicked(hero: Hero) {
        val intent = Intent(this, HeroDetailActivity::class.java)
        intent.putExtra("hero_id", hero.id)
        startActivity(intent)
    }
}