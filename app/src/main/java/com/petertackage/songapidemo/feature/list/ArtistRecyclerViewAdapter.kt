package com.petertackage.songapidemo.feature.list

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.petertackage.songapidemo.databinding.ArtistListItemBinding
import com.petertackage.songapidemo.service.Artist

class ArtistRecyclerViewAdapter(
    diffItemCallback: DiffUtil.ItemCallback<Artist>
) : ListAdapter<Artist, ArtistRecyclerViewAdapter.ViewHolder>(diffItemCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ArtistListItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent, false
        );
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.nameView.text = item.name
        holder.trackCountView.text = item.trackCount.toString()
        holder.followersCountView.text = item.followersCount.toString()

        Glide.with(holder.itemView)
            .load(item.avatarUrl)
            .into(holder.avatarView);
    }

    inner class ViewHolder(binding: ArtistListItemBinding) : RecyclerView.ViewHolder(binding.root) {
        val nameView: TextView = binding.textViewArtistListItemName
        val trackCountView: TextView = binding.textViewArtistListItemTrackCount
        val followersCountView: TextView = binding.textViewArtistListItemFollowerCount
        val avatarView: ImageView = binding.imageViewArtistListItemAvatar
    }

}
