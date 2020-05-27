package com.petertackage.songapidemo

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import com.petertackage.songapidemo.feature.audio.StreamPlayer
import com.petertackage.songapidemo.feature.audio.provideStreamPlayer
import com.petertackage.songapidemo.service.Track

class MainActivityViewModel(private val streamPlayer: StreamPlayer = provideStreamPlayer()) :
    ViewModel() {
    val state: LiveData<MainActivityState>
        get() =
            streamPlayer.state.map { playerState ->
                MainActivityState(
                    Track(
                        "id", "title",
                        "genre", "artwork", 100, "wavformUrl", "streamUrl"
                    )
                )
            }
}

data class MainActivityState(val activeTrack: Track?)