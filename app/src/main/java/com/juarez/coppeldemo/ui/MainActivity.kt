package com.juarez.coppeldemo.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import com.juarez.coppeldemo.adapters.HeroLoadStateAdapter
import com.juarez.coppeldemo.adapters.HeroesAdapter
import com.juarez.coppeldemo.databinding.ActivityMainBinding
import com.juarez.coppeldemo.models.Hero
import com.juarez.coppeldemo.repositories.HeroRepository
import com.juarez.coppeldemo.viewmodels.HeroViewModel
import com.juarez.coppeldemo.viewmodels.HeroViewModelFactory
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: HeroViewModel
    private val heroesAdapter = HeroesAdapter { onItemClicked(it) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val provider = HeroViewModelFactory(HeroRepository());
        viewModel = ViewModelProvider(this, provider).get(HeroViewModel::class.java)

        binding.recyclerHeroes.apply {
            layoutManager = GridLayoutManager(context, 2)
            adapter = heroesAdapter.withLoadStateFooter(
                footer = HeroLoadStateAdapter(heroesAdapter::retry)
            )
        }
        lifecycleScope.launch {
            viewModel.heroes.collectLatest { heroesAdapter.submitData(it) }
        }

        heroesAdapter.addLoadStateListener {
            val isLoading = it.refresh is LoadState.Loading
            binding.progressBarHeroes.isVisible = isLoading
        }
    }

    private fun onItemClicked(hero: Hero) {
        val intent = Intent(this, HeroDetailActivity::class.java)
        intent.putExtra("hero_id", hero.id.toInt())
        intent.putExtra("hero_url", hero.image.url)
        startActivity(intent)
    }
}