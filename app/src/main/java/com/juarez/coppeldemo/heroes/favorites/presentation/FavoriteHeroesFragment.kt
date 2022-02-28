package com.juarez.coppeldemo.heroes.favorites.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.juarez.coppeldemo.databinding.FragmentFavoriteHeroesBinding
import com.juarez.coppeldemo.heroes.data.Hero
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FavoriteHeroesFragment : Fragment() {
    private val viewModel: FavoriteHeroesViewModel by viewModels()
    private var _binding: FragmentFavoriteHeroesBinding? = null
    private val binding get() = _binding!!
    private val favoritesAdapter =
        FavoritesAdapter({ onItemClicked(it) }, { onItemRemoved(it) })

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentFavoriteHeroesBinding.inflate(inflater, container, false)

        binding.recyclerFavorites.apply {
            layoutManager = GridLayoutManager(requireContext(), 2)
            adapter = favoritesAdapter
            setHasFixedSize(true)
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.favoriteHeroes
                .flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.STARTED)
                .collectLatest { favoritesAdapter.submitList(it) }
        }
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun onItemClicked(hero: Hero) {
        val action =
            FavoriteHeroesFragmentDirections.actionFavoriteHeroesFragmentToBiographyFragment(
                hero.id.toInt(),
                hero.name
            )
        findNavController().navigate(action)
    }

    private fun onItemRemoved(heroId: Int) {
        viewModel.removeFavoriteHero(heroId)
    }
}