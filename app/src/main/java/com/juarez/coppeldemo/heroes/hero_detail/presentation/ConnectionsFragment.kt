package com.juarez.coppeldemo.heroes.hero_detail.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.juarez.coppeldemo.databinding.FragmentConnectionsBinding
import com.juarez.coppeldemo.heroes.data.Hero
import com.juarez.coppeldemo.utils.Constants
import com.juarez.coppeldemo.utils.loadImage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ConnectionsFragment : Fragment() {
    private val viewModel: HeroDetailViewModel by activityViewModels()
    private var _binding: FragmentConnectionsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentConnectionsBinding.inflate(inflater, container, false)

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.heroState.collect {
                        if (it is HeroState.Success) updateConnectionsData(it.data)
                    }
                }
                launch {
                    viewModel.url.collect { binding.imgConnPhoto.loadImage(it) }
                }
            }
        }
        return binding.root
    }

    private fun updateConnectionsData(hero: Hero) {
        binding.apply {
            with(hero.connections) {
                txtAffiliations.text = groupAffiliation ?: Constants.NO_AVAILABLE
                txtRelatives.text = relatives ?: Constants.NO_AVAILABLE
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}