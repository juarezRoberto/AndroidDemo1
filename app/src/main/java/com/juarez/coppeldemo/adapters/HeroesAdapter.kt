package com.juarez.coppeldemo.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.juarez.coppeldemo.databinding.ItemHeroBinding
import com.juarez.coppeldemo.models.Hero
import com.squareup.picasso.Picasso

class HeroesAdapter(
    private val heroes: ArrayList<Hero>,
    private val onItemClicked: (Hero) -> Unit
) :
     RecyclerView.Adapter<HeroesAdapter.ViewHolder>() {

    fun updateData(data: List<Hero>) {
        heroes.clear()
        heroes.addAll(data)
        notifyDataSetChanged()
    }

    inner class ViewHolder(val binding: ItemHeroBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemHeroBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder) {
            with(heroes[position]) {
                binding.txtName.text = name
                Picasso.get().load(image.url).into(binding.imgPhoto)
            }
            itemView.setOnClickListener { onItemClicked(heroes[position]) }
        }
    }

    override fun getItemCount() = heroes.size
}