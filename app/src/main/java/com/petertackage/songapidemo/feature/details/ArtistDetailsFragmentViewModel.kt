package com.petertackage.songapidemo.feature.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.petertackage.songapidemo.async.CoroutineDispatcherProvider
import com.petertackage.songapidemo.feature.audio.StreamPlayer
import com.petertackage.songapidemo.feature.audio.provideStreamPlayer
import com.petertackage.songapidemo.service.Artist
import com.petertackage.songapidemo.service.ArtistService
import com.petertackage.songapidemo.service.Track
import com.petertackage.songapidemo.service.provideArtistService
import kotlinx.coroutines.launch

class ArtistDetailsFragmentViewModel(
    private val artistService: ArtistService = provideArtistService(),
    private val streamPlayer: StreamPlayer = provideStreamPlayer(),
    private val dispatcherProvider: CoroutineDispatcherProvider = CoroutineDispatcherProvider()
) : ViewModel() {

    private val _state = MutableLiveData<ArtistDetailsState>()
    val state: LiveData<ArtistDetailsState> get() = _state

    fun loadArtist(artistName: String) {
        fetchArtist(artistName)
    }

    fun playTrack(url: String) {
        streamPlayer.play(url)
    }

    fun stopTrack() {
        streamPlayer.stop()
    }

    private fun fetchArtist(artistName: String) {
        emitNow(ArtistDetailsState.IsLoading)
        viewModelScope.launch(dispatcherProvider.io) {
            Result.runCatching { fetchArtistDetails(artistName) }
                .onSuccess { emit(it) }
                .onFailure { emit(ArtistDetailsState.Failed(it)) }
        }
    }

    private suspend fun fetchArtistDetails(artistName: String): ArtistDetailsState.Loaded {
        val artist = artistService.artist(artistName);
        val tracks = artistService.tracksForArtist(artistName);
        return ArtistDetailsState.Loaded(artist, tracks)
    }

    private fun emitNow(state: ArtistDetailsState) {
        _state.value = state
    }

    private fun emit(state: ArtistDetailsState) {
        _state.postValue(state)
    }

}

sealed class ArtistDetailsState {
    object IsLoading : ArtistDetailsState()
    data class Loaded(
        val artist: Artist,
        val tracks: List<Track>
    ) : ArtistDetailsState()

    data class Failed(val throwable: Throwable) : ArtistDetailsState()
}

