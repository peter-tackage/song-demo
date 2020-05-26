package com.petertackage.songapidemo.feature.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.petertackage.songapidemo.async.CoroutineDispatcherProvider
import com.petertackage.songapidemo.service.Artist
import com.petertackage.songapidemo.service.ArtistService
import com.petertackage.songapidemo.service.Track
import com.petertackage.songapidemo.service.provideArtistService
import kotlinx.coroutines.launch
import timber.log.Timber

class ArtistDetailsFragmentViewModel(
    private val artistService: ArtistService = provideArtistService(),
    private val dispatcherProvider: CoroutineDispatcherProvider = CoroutineDispatcherProvider()
) : ViewModel() {

    private val _artistDetailsState = MutableLiveData<ArtistDetailsState>()
    val artistDetailsState: LiveData<ArtistDetailsState> get() = _artistDetailsState

    fun loadArtist(artistName: String) {
        viewModelScope.launch(dispatcherProvider.io) { fetchArtist(artistName) }
    }

    private suspend fun fetchArtist(artistName: String) {
        try {
            val artist = artistService.artist(artistName)
            val tracks = artistService.tracksForArtist(artistName)

            _artistDetailsState.postValue(ArtistDetailsState(artist, tracks))
        } catch (e: Exception) {
            Timber.e(e, "Service failure: $e")
        }
    }
}

data class ArtistDetailsState(
    val artist: Artist,
    val tracks: List<Track>
)