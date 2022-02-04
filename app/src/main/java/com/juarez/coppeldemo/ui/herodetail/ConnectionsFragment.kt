package com.juarez.coppeldemo.ui.herodetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.juarez.coppeldemo.data.models.Hero
import com.juarez.coppeldemo.databinding.FragmentConnectionsBinding
import com.juarez.coppeldemo.ui.sharedviewmodels.HeroDetailViewModel
import com.juarez.coppeldemo.utils.Constants
import com.juarez.coppeldemo.utils.loadImage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

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

        lifecycleScope.launchWhenStarted {
            viewModel.heroState.collect {
                if (it is HeroState.Success) updateConnectionsData(it.data)
            }
        }
        lifecycleScope.launchWhenStarted {
            viewModel.url.collect { binding.imgConnPhoto.loadImage(it) }
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