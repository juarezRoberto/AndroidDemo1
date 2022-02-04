package com.juarez.coppeldemo.data.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.juarez.coppeldemo.R
import com.juarez.coppeldemo.data.models.Hero
import com.juarez.coppeldemo.databinding.ItemHeroBinding
import com.squareup.picasso.Picasso

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
                Picasso.get()
                    .load(favorite.image.url)
                    .error(R.drawable.hero_placeholder)
                    .into(binding.imgPhoto)
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