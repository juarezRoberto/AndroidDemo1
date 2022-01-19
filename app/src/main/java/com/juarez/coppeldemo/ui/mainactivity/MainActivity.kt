package com.juarez.coppeldemo.ui.mainactivity

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import com.juarez.coppeldemo.adapters.HeroLoadStateAdapter
import com.juarez.coppeldemo.adapters.HeroesAdapter
import com.juarez.coppeldemo.databinding.ActivityMainBinding
import com.juarez.coppeldemo.models.Hero
import com.juarez.coppeldemo.ui.herodetailactivity.HeroDetailActivity
import com.juarez.coppeldemo.utils.Constants
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel: HeroViewModel by viewModels()
    private val heroesAdapter = HeroesAdapter { onItemClicked(it) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

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
        intent.putExtra(Constants.EXTRA_HERO_ID, hero.id.toInt())
        intent.putExtra(Constants.EXTRA_HERO_URL, hero.image.url)
        startActivity(intent)
    }
}