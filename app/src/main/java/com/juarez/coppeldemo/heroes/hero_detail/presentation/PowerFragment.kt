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
import com.juarez.coppeldemo.databinding.FragmentPowerBinding
import com.juarez.coppeldemo.heroes.data.Hero
import com.juarez.coppeldemo.utils.convertToInt
import com.juarez.coppeldemo.utils.loadImage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PowerFragment : Fragment() {
    private val viewModel: HeroDetailViewModel by activityViewModels()
    private var _binding: FragmentPowerBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentPowerBinding.inflate(inflater, container, false)

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.heroState.collect {
                        if (it is HeroState.Success) updatePowerStatesData(it.data)
                    }
                }
                launch {
                    viewModel.url.collect { binding.imgPowerPhoto.loadImage(it) }
                }
            }
        }
        return binding.root
    }

    private fun updatePowerStatesData(hero: Hero) {
        binding.apply {
            with(hero.powerStats) {
                progressIntelligence.progress = intelligence?.convertToInt() ?: 0
                progressStrength.progress = strength?.convertToInt() ?: 0
                progressSpeed.progress = speed?.convertToInt() ?: 0
                progressDurability.progress = durability?.convertToInt() ?: 0
                progressPower.progress = power?.convertToInt() ?: 0
                progressCombat.progress = combat?.convertToInt() ?: 0
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}