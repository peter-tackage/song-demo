package com.petertackage.songapidemo.feature.list

import androidx.recyclerview.widget.DiffUtil

fun <T> provideDiffItemCallback(): DiffUtil.ItemCallback<T> {
    return object : DiffUtil.ItemCallback<T>() {
        override fun areItemsTheSame(
            oldItem: T,
            newItem: T
        ): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: T,
            newItem: T
        ): Boolean {
            // No local state.
            return true
        }
    }
}