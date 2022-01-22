package com.juarez.coppeldemo.ui.herodetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.juarez.coppeldemo.databinding.FragmentAppearanceBinding
import com.juarez.coppeldemo.ui.sharedviewmodels.HeroDetailViewModel
import com.juarez.coppeldemo.utils.Constants
import com.juarez.coppeldemo.utils.loadImage

class AppearanceFragment : Fragment() {
    private val viewModel: HeroDetailViewModel by activityViewModels()
    private var _binding: FragmentAppearanceBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAppearanceBinding.inflate(inflater, container, false)
        viewModel.hero.observe(viewLifecycleOwner, {
            with(binding) {
                with(it.appearance) {
                    txtGender.text = gender ?: Constants.NO_AVAILABLE
                    txtRace.text = race ?: Constants.NO_AVAILABLE
                    txtHeight.text = height?.toString() ?: Constants.NO_AVAILABLE
                    txtWeight.text = weight?.toString() ?: Constants.NO_AVAILABLE
                    txtEyeColor.text = eyeColor ?: Constants.NO_AVAILABLE
                    txtHairColor.text = hairColor ?: Constants.NO_AVAILABLE
                }
            }
        })
        viewModel.url.observe(viewLifecycleOwner, { binding.imgAppearancePhoto.loadImage(it) })
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}