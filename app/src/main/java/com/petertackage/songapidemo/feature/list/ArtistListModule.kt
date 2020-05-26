package com.petertackage.songapidemo.feature.list

import androidx.recyclerview.widget.DiffUtil
import com.petertackage.songapidemo.service.Artist

fun provideDiffItemCallback(): DiffUtil.ItemCallback<Artist> {
    return object : DiffUtil.ItemCallback<Artist>() {
        override fun areItemsTheSame(
            oldItem: Artist,
            newItem: Artist
        ): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: Artist,
            newItem: Artist
        ): Boolean {
            // No local state.
            return true
        }
    }
}