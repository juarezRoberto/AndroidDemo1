package com.juarez.coppeldemo.data.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.juarez.coppeldemo.databinding.ItemHeroLoadingStateBinding

class HeroLoadStateAdapter(private val retry: () -> Unit) :
    LoadStateAdapter<HeroLoadStateAdapter.HeroLoadStateViewHolder>() {

    inner class HeroLoadStateViewHolder(
        val binding: ItemHeroLoadingStateBinding,
    ) : RecyclerView.ViewHolder(binding.root)

    override fun onBindViewHolder(holder: HeroLoadStateViewHolder, loadState: LoadState) {
        with(holder) {
            Log.d("state", loadState.toString())
            if (loadState is LoadState.Error) {
                binding.txtError.text = loadState.error.localizedMessage
            }
            binding.progressLoadHeroes.isVisible = (loadState is LoadState.Loading)
            binding.btnRetry.isVisible = (loadState is LoadState.Error)
            binding.txtError.isVisible = (loadState is LoadState.Error)
            binding.btnRetry.setOnClickListener {
                retry()
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        loadState: LoadState
    ) = HeroLoadStateViewHolder(
        ItemHeroLoadingStateBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
    )
}