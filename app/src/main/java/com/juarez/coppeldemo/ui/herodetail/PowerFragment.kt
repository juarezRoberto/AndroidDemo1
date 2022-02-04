package com.juarez.coppeldemo.ui.herodetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.juarez.coppeldemo.data.models.Hero
import com.juarez.coppeldemo.databinding.FragmentPowerBinding
import com.juarez.coppeldemo.ui.sharedviewmodels.HeroDetailViewModel
import com.juarez.coppeldemo.utils.convertToInt
import com.juarez.coppeldemo.utils.loadImage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

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
        lifecycleScope.launchWhenStarted {
            viewModel.heroState.collect {
                if (it is HeroState.Success) updatePowerStatesData(it.data)
            }
        }
        lifecycleScope.launchWhenStarted {
            viewModel.url.collect {
                binding.imgPowerPhoto.loadImage(it)
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