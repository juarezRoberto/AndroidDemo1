package com.juarez.coppeldemo.ui.heroes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import com.juarez.coppeldemo.R
import com.juarez.coppeldemo.adapters.HeroLoadStateAdapter
import com.juarez.coppeldemo.adapters.HeroesAdapter
import com.juarez.coppeldemo.databinding.FragmentHeroesBinding
import com.juarez.coppeldemo.models.Hero
import com.juarez.coppeldemo.utils.Constants
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HeroesFragment : Fragment() {
    private var _binding: FragmentHeroesBinding? = null
    private val binding get() = _binding!!
    private val viewModel: HeroViewModel by viewModels()
    private val heroesAdapter = HeroesAdapter { onItemClicked(it) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHeroesBinding.inflate(inflater, container, false)

        binding.recyclerHeroes.apply {
            layoutManager = GridLayoutManager(context, 2)
            adapter = heroesAdapter.withLoadStateFooter(
                footer = HeroLoadStateAdapter(heroesAdapter::retry)
            )
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.heroes.collectLatest { heroesAdapter.submitData(it) }
        }

        heroesAdapter.addLoadStateListener {
            val isLoading = it.refresh is LoadState.Loading
            binding.progressBarHeroes.isVisible = isLoading
        }
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun onItemClicked(hero: Hero) {
        findNavController().navigate(
            R.id.action_heroesFragment_to_heroDetailFragment,
            bundleOf(
                Constants.BUNDLE_HERO_ID to hero.id.toInt(),
                Constants.BUNDLE_HERO_URL to hero.image.url
            )
        )
    }
}