package com.juarez.coppeldemo.heroes.favorites.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.juarez.coppeldemo.databinding.ItemHeroBinding
import com.juarez.coppeldemo.heroes.data.Hero
import com.juarez.coppeldemo.utils.loadImage

class FavoritesAdapter(
    private val onItemClickListener: (user: Hero) -> Unit,
    private val onRemoveItemClickListener: (userId: Int) -> Unit,
) : ListAdapter<Hero, FavoritesAdapter.ViewHolder>(DiffCallback()) {

    class ViewHolder(val binding: ItemHeroBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemHeroBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val favorite = getItem(position)
        with(holder) {

            binding.txtName.text = favorite.name
            if (!favorite.image.url.isNullOrEmpty()) {
                binding.imgPhoto.loadImage(favorite.image.url)
            }

            binding.btnRemoveFavorite.setOnClickListener {
                onRemoveItemClickListener(favorite.id.toInt())
            }

            this.itemView.setOnClickListener { onItemClickListener(favorite) }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<Hero>() {
        override fun areItemsTheSame(oldItem: Hero, newItem: Hero) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Hero, newItem: Hero) =
            oldItem == newItem
    }
}