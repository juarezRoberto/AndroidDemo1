package com.juarez.coppeldemo.ui.herodetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.juarez.coppeldemo.data.models.Hero
import com.juarez.coppeldemo.data.models.Image
import com.juarez.coppeldemo.databinding.FragmentBiographyBinding
import com.juarez.coppeldemo.ui.sharedviewmodels.HeroDetailViewModel
import com.juarez.coppeldemo.utils.Constants
import com.juarez.coppeldemo.utils.loadImage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class BiographyFragment : Fragment() {
    private val viewModel: HeroDetailViewModel by activityViewModels()
    private val args: BiographyFragmentArgs by navArgs()
    private var _binding: FragmentBiographyBinding? = null
    private val binding get() = _binding!!
    private var heroId = 0
    private var heroName = ""
    private var heroUrl = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentBiographyBinding.inflate(inflater, container, false)
        heroId = args.heroId
        onClickActions()

        viewModel.getHeroDetail(heroId)

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.heroState.collect {
                        when (it) {
                            is HeroState.Loading -> {
                                binding.progressBarHeroDetail.isVisible = true
                            }
                            is HeroState.Error -> {
                                if (it.message.isNotEmpty()) {
                                    binding.progressBarHeroDetail.isVisible = false
                                    errorAlert(it.message)
                                }
                            }
                            is HeroState.Success -> {
                                binding.progressBarHeroDetail.isVisible = false
                                updateHeroData(it.data)
                            }
                            else -> Unit
                        }
                    }
                }

                launch {
                    viewModel.isFavorite.collect {
                        binding.fabAddFavorites.isVisible = it
                    }
                }
            }
        }

        return binding.root
    }

    private fun onClickActions() {
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
                Hero(id = "$heroId", name = heroName, image = Image(heroUrl)))
        }
    }

    private fun updateHeroData(hero: Hero) {
        with(binding) {
            with(hero.biography) {
                heroName = name ?: Constants.NO_AVAILABLE
                txtBioName.text = name ?: Constants.NO_AVAILABLE
                txtFullName.text =
                    if (fullName.isNullOrEmpty()) Constants.NO_AVAILABLE else fullName
                txtPlaceBirth.text = placeOfBirth ?: Constants.NO_AVAILABLE
                txtAppearance.text = firstAppearance ?: Constants.NO_AVAILABLE
                txtPublisher.text = publisher ?: Constants.NO_AVAILABLE
                txtAlignment.text = alignment ?: Constants.NO_AVAILABLE
            }
            with(hero.image) {
                heroUrl = url ?: Constants.NO_AVAILABLE
                imgDetailPhoto.loadImage(url)
            }
        }
    }

    private fun errorAlert(message: String) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle(Constants.ALERT_TITLE)
        builder.setMessage(message)
        builder.setPositiveButton(Constants.ALERT_RETRY) { dialog, _ ->
            viewModel.getHeroDetail(heroId)
            dialog.dismiss()
        }
        builder.setNegativeButton(Constants.ALERT_CANCEL) { dialog, _ ->
            dialog.dismiss()
        }
        builder.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}