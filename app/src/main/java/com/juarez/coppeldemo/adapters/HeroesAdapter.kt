package com.juarez.coppeldemo.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.juarez.coppeldemo.R
import com.juarez.coppeldemo.databinding.ItemHeroBinding
import com.juarez.coppeldemo.models.Hero
import com.squareup.picasso.Picasso

class HeroesAdapter(private val onItemClicked: (Hero) -> Unit) :
    PagingDataAdapter<Hero, HeroesAdapter.HeroViewHolder>(HeroComparator) {

    inner class HeroViewHolder(val binding: ItemHeroBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HeroViewHolder {
        val binding = ItemHeroBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HeroViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HeroViewHolder, position: Int) {
        val item = getItem(position)
        item?.let {
            with(holder) {
                binding.txtName.text = it.name
                Picasso.get()
                    .load(it.image.url)
                    .error(R.drawable.hero_placeholder)
                    .into(binding.imgPhoto)
                this.itemView.setOnClickListener { onItemClicked(item) }
            }

        }
    }

    object HeroComparator : DiffUtil.ItemCallback<Hero>() {
        override fun areItemsTheSame(oldItem: Hero, newItem: Hero): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Hero, newItem: Hero): Boolean {
            return oldItem == newItem
        }
    }
}