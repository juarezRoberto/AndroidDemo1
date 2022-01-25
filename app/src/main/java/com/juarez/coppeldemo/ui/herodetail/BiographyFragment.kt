package com.juarez.coppeldemo.ui.herodetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.juarez.coppeldemo.data.models.Hero
import com.juarez.coppeldemo.data.models.Image
import com.juarez.coppeldemo.databinding.FragmentBiographyBinding
import com.juarez.coppeldemo.ui.sharedviewmodels.HeroDetailViewModel
import com.juarez.coppeldemo.utils.Constants
import com.juarez.coppeldemo.utils.loadImage
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BiographyFragment : Fragment() {
    private val viewModel: HeroDetailViewModel by activityViewModels()
    private val args: BiographyFragmentArgs by navArgs()
    private var _binding: FragmentBiographyBinding? = null
    private val binding get() = _binding!!
    private var heroId = 0
    private var heroName = ""
    private var heroUrl = ""
    private var isFavorite = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBiographyBinding.inflate(inflater, container, false)
        heroId = args.heroId

        binding.btnShowPower.setOnClickListener {
            val action = BiographyFragmentDirections.actionBiographyFragmentToPowerFragment()
            it.findNavController().navigate(action)
        }
        binding.btnShowAppearance.setOnClickListener {
            val action = BiographyFragmentDirections.actionBiographyFragmentToAppearanceFragment()
            it.findNavController().navigate(action)
        }
        binding.btnShowConnections.setOnClickListener {
            val action = BiographyFragmentDirections.actionBiographyFragmentToConnectionsFragment()
            it.findNavController().navigate(action)
        }
        binding.fabAddFavorites.setOnClickListener {
            viewModel.saveFavoriteHero(
                Hero(id = "$heroId", name = heroName, image = Image(heroUrl))
            )
        }
        viewModel.getHeroDetail(heroId)
        viewModel.hero.observe(viewLifecycleOwner, {
            with(binding) {
                with(it.biography) {
                    heroName = name ?: Constants.NO_AVAILABLE
                    txtBioName.text = name ?: Constants.NO_AVAILABLE
                    txtFullName.text =
                        if (fullName.isNullOrEmpty()) Constants.NO_AVAILABLE else fullName
                    txtPlaceBirth.text = placeOfBirth ?: Constants.NO_AVAILABLE
                    txtAppearance.text = firstAppearance ?: Constants.NO_AVAILABLE
                    txtPublisher.text = publisher ?: Constants.NO_AVAILABLE
                    txtAlignment.text = alignment ?: Constants.NO_AVAILABLE
                }
                with(it.image) {
                    heroUrl = url ?: Constants.NO_AVAILABLE
                    imgDetailPhoto.loadImage(url)
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
        viewModel.isFavorite.observe(viewLifecycleOwner, {
            binding.fabAddFavorites.isVisible = it
        })
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}