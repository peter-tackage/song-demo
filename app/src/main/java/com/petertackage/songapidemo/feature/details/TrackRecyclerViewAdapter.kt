package com.petertackage.songapidemo.feature.details

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.petertackage.songapidemo.databinding.TrackListItemBinding
import com.petertackage.songapidemo.service.Track

class TrackRecyclerViewAdapter(
    diffItemCallback: DiffUtil.ItemCallback<Track>
) : ListAdapter<Track, TrackRecyclerViewAdapter.ViewHolder>(diffItemCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = TrackListItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)

        with(holder) {
            titleView.text = item.title
            durationView.text = android.text.format.DateUtils.formatElapsedTime(item.duration)
            genreView.text = item.genre
            Glide.with(itemView)
                .load(item.artworkUrl)
                .into(trackAvatarImageView)
            // TODO Call player
            itemView.setOnClickListener {
                //viewModel.playTrack(track.streamUrl)
            }
        }
    }

    inner class ViewHolder(binding: TrackListItemBinding) : RecyclerView.ViewHolder(binding.root) {
        val titleView: TextView = binding.textViewTrackListItemTitle
        val durationView: TextView = binding.textViewTrackListItemLength
        val genreView: TextView = binding.textViewTrackListItemGenre
        val trackAvatarImageView: ImageView = binding.imageViewTrackListItemAvatar
    }

}
