package com.juarez.coppeldemo.data.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.juarez.coppeldemo.R
import com.juarez.coppeldemo.data.models.Hero
import com.juarez.coppeldemo.databinding.ItemHeroBinding
import com.squareup.picasso.Picasso

class FavoritesAdapter(
    private val data: ArrayList<Hero>,
    private val onItemClickListener: (user: Hero) -> Unit,
    private val onRemoveItemClickListener: (userId: Int) -> Unit
) : RecyclerView.Adapter<FavoritesAdapter.ViewHolder>() {

    fun updateData(newData: List<Hero>) {
        data.clear()
        data.addAll(newData)
        notifyDataSetChanged()
    }

    inner class ViewHolder(val binding: ItemHeroBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemHeroBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder) {
            with(data[position]) {
                binding.txtName.text = name
                Log.d("url", name + image.url)

                if (!image.url.isNullOrEmpty()) {
                    Picasso.get()
                        .load(image.url)
                        .error(R.drawable.hero_placeholder)
                        .into(binding.imgPhoto)
                }

                binding.btnRemoveFavorite.setOnClickListener {
                    onRemoveItemClickListener(this.id.toInt())
                }
            }
            this.itemView.setOnClickListener { onItemClickListener(data[position]) }
        }
    }

    override fun getItemCount() = data.size
}