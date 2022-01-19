package com.juarez.coppeldemo.ui.herodetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.juarez.coppeldemo.databinding.FragmentHeroDetailBinding
import com.juarez.coppeldemo.extensions.convertToInt
import com.juarez.coppeldemo.extensions.loadImage
import com.juarez.coppeldemo.utils.Constants
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HeroDetailFragment : Fragment() {

    private var _binding: FragmentHeroDetailBinding? = null
    private val binding get() = _binding!!
    private val viewModel: HeroDetailViewModel by viewModels()
    private var heroId = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHeroDetailBinding.inflate(inflater, container, false)
        arguments?.getInt(Constants.BUNDLE_HERO_ID)?.let {
            heroId = it
        }
        arguments?.getString(Constants.BUNDLE_HERO_URL)?.let {
            binding.imgDetailPhoto.loadImage(it)
        }

        viewModel.getHeroDetail(heroId)
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
                with(it.biography) {
                    txtBioName.text = name ?: Constants.NO_AVAILABLE
                    txtFullName.text =
                        if (fullName.isNullOrEmpty()) Constants.NO_AVAILABLE else fullName
                    txtPlaceBirth.text = placeOfBirth ?: Constants.NO_AVAILABLE
                    txtAppearance.text = firstAppearance ?: Constants.NO_AVAILABLE
                    txtPublisher.text = publisher ?: Constants.NO_AVAILABLE
                    txtAlignment.text = alignment ?: Constants.NO_AVAILABLE
                }
                with(it.appearance) {
                    txtGender.text = gender ?: Constants.NO_AVAILABLE
                    txtRace.text = race ?: Constants.NO_AVAILABLE
                    txtHeight.text = height?.toString() ?: Constants.NO_AVAILABLE
                    txtWeight.text = weight?.toString() ?: Constants.NO_AVAILABLE
                    txtEyeColor.text = eyeColor ?: Constants.NO_AVAILABLE
                    txtHairColor.text = hairColor ?: Constants.NO_AVAILABLE
                }
                with(it.connections) {
                    txtAffiliations.text = groupAffiliation ?: Constants.NO_AVAILABLE
                    txtRelatives.text = relatives ?: Constants.NO_AVAILABLE
                }
            }
        })
        viewModel.loading.observe(viewLifecycleOwner, {
            binding.progressBarHeroDetail.isVisible = it
        })
        viewModel.error.observe(viewLifecycleOwner, {
            val builder = AlertDialog.Builder(requireContext())
            builder.setTitle(Constants.ALERT_TITLE)
            builder.setMessage(it)
            builder.setPositiveButton(Constants.ALERT_RETRY) { dialog, _ ->
                viewModel.getHeroDetail(heroId)
                dialog.dismiss()
            }
            builder.setNegativeButton(Constants.ALERT_CANCEL) { dialog, _ ->
                dialog.dismiss()
            }
            builder.show()
        })

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}