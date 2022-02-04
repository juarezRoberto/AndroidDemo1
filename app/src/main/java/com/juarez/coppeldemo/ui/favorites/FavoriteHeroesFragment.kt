package com.juarez.coppeldemo.ui.favorites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.juarez.coppeldemo.data.adapters.FavoritesAdapter
import com.juarez.coppeldemo.data.models.Hero
import com.juarez.coppeldemo.databinding.FragmentFavoriteHeroesBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

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
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.favoriteHeroes.collectLatest {
                favoritesAdapter.submitList(it)
            }
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