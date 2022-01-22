package com.juarez.coppeldemo.ui.herodetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.juarez.coppeldemo.databinding.FragmentConnectionsBinding
import com.juarez.coppeldemo.ui.sharedviewmodels.HeroDetailViewModel
import com.juarez.coppeldemo.utils.Constants
import com.juarez.coppeldemo.utils.loadImage
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ConnectionsFragment : Fragment() {
    private val viewModel: HeroDetailViewModel by activityViewModels()
    private var _binding: FragmentConnectionsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentConnectionsBinding.inflate(inflater, container, false)

        viewModel.hero.observe(viewLifecycleOwner, {
            with(binding) {
                with(it.connections) {
                    txtAffiliations.text = groupAffiliation ?: Constants.NO_AVAILABLE
                    txtRelatives.text = relatives ?: Constants.NO_AVAILABLE
                }
            }
        })

        viewModel.url.observe(viewLifecycleOwner, { binding.imgConnPhoto.loadImage(it) })
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}