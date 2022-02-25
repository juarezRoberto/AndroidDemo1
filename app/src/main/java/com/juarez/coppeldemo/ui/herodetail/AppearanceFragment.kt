package com.juarez.coppeldemo.ui.herodetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.juarez.coppeldemo.data.models.Hero
import com.juarez.coppeldemo.databinding.FragmentAppearanceBinding
import com.juarez.coppeldemo.ui.sharedviewmodels.HeroDetailViewModel
import com.juarez.coppeldemo.utils.Constants
import com.juarez.coppeldemo.utils.loadImage
import kotlinx.coroutines.launch

class AppearanceFragment : Fragment() {
    private val viewModel: HeroDetailViewModel by activityViewModels()
    private var _binding: FragmentAppearanceBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentAppearanceBinding.inflate(inflater, container, false)

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.heroState.collect {
                        if (it is HeroState.Success) updateAppearanceData(it.data)
                    }
                }
                launch {
                    viewModel.url.collect { binding.imgAppearancePhoto.loadImage(it) }
                }
            }
        }
        return binding.root
    }

    private fun updateAppearanceData(hero: Hero) {
        binding.apply {
            with(hero.appearance) {
                txtGender.text = gender ?: Constants.NO_AVAILABLE
                txtRace.text = race ?: Constants.NO_AVAILABLE
                txtHeight.text = height?.toString() ?: Constants.NO_AVAILABLE
                txtWeight.text = weight?.toString() ?: Constants.NO_AVAILABLE
                txtEyeColor.text = eyeColor ?: Constants.NO_AVAILABLE
                txtHairColor.text = hairColor ?: Constants.NO_AVAILABLE
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}