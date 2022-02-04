package com.juarez.coppeldemo.ui.heroes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import com.juarez.coppeldemo.data.adapters.HeroLoadStateAdapter
import com.juarez.coppeldemo.data.adapters.HeroesAdapter
import com.juarez.coppeldemo.data.models.Hero
import com.juarez.coppeldemo.databinding.FragmentHeroesBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class HeroesFragment : Fragment() {
    private var _binding: FragmentHeroesBinding? = null
    private val binding get() = _binding!!
    private val viewModel: HeroViewModel by viewModels()
    private val heroesAdapter = HeroesAdapter { onItemClicked(it) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentHeroesBinding.inflate(inflater, container, false)

        binding.containerHeroesError.isVisible = false
        binding.btnRetryGetHeroes.setOnClickListener {
            heroesAdapter.retry()
        }
        binding.fabShowFavorites.setOnClickListener {
            val action = HeroesFragmentDirections.actionHeroesFragmentToFavoriteHeroesFragment()
            it.findNavController().navigate(action)
        }
        binding.recyclerHeroes.apply {
            layoutManager = GridLayoutManager(requireContext(), 2)
            adapter = heroesAdapter.withLoadStateFooter(
                footer = HeroLoadStateAdapter(heroesAdapter::retry)
            )
        }
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.heroes.collectLatest { heroesAdapter.submitData(it) }
        }

        heroesAdapter.addLoadStateListener {
            if (it.refresh is LoadState.Loading) {
                binding.containerHeroesError.isVisible = false
                binding.progressBarHeroes.isVisible = true
            } else {
                binding.progressBarHeroes.isVisible = false
            }
            if (it.refresh is LoadState.Error) {
                binding.containerHeroesError.isVisible = true
                val error = (it.refresh as LoadState.Error).error.localizedMessage
                binding.txtHeroesError.text = error
            }
        }
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun onItemClicked(hero: Hero) {
        val action = HeroesFragmentDirections.actionHeroesFragmentToBiographyFragment(
            hero.id.toInt(),
            hero.name
        )
        findNavController().navigate(action)
//    navigate with action id
//        findNavController().navigate(
//            R.id.action_heroesFragment_to_heroDetailFragment,
//            bundleOf(
//                Constants.BUNDLE_HERO_ID to hero.id.toInt(),
//                Constants.BUNDLE_HERO_URL to hero.image.url
//            )
//        )
    }
}