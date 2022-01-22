package com.juarez.coppeldemo.ui.herodetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.juarez.coppeldemo.databinding.FragmentPowerBinding
import com.juarez.coppeldemo.ui.sharedviewmodels.HeroDetailViewModel
import com.juarez.coppeldemo.utils.convertToInt
import com.juarez.coppeldemo.utils.loadImage
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PowerFragment : Fragment() {
    private val viewModel: HeroDetailViewModel by activityViewModels()
    private var _binding: FragmentPowerBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPowerBinding.inflate(inflater, container, false)
        viewModel.hero.observe(viewLifecycleOwner, {
            with(binding) {
                with(it.powerStats) {
                    progressIntelligence.progress = intelligence?.convertToInt() ?: 0
                    progressStrength.progress = strength?.convertToInt() ?: 0
                    progressSpeed.progress = speed?.convertToInt() ?: 0
                    progressDurability.progress = durability?.convertToInt() ?: 0
                    progressPower.progress = power?.convertToInt() ?: 0
                    progressCombat.progress = combat?.convertToInt() ?: 0
                }
            }
        })
        viewModel.url.observe(viewLifecycleOwner, { binding.imgPowerPhoto.loadImage(it) })
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}