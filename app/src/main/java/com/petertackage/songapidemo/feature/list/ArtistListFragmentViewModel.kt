package com.petertackage.songapidemo.feature.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.petertackage.songapidemo.async.CoroutineDispatcherProvider
import com.petertackage.songapidemo.service.Artist
import com.petertackage.songapidemo.service.ArtistService
import com.petertackage.songapidemo.service.provideArtistService
import kotlinx.coroutines.launch
import timber.log.Timber

class ArtistListFragmentViewModel(
    private val artistService: ArtistService = provideArtistService(),
    dispatcherProvider: CoroutineDispatcherProvider = CoroutineDispatcherProvider()
) : ViewModel() {

    private val _artistList = MutableLiveData<List<Artist>>()
    val artistList: LiveData<List<Artist>> get() = _artistList

    init {
        viewModelScope.launch(dispatcherProvider.io) { fetchTopArtists() }
    }

    private suspend fun fetchTopArtists() {
        try {
            val artists = artistService.topArtists()
            Timber.d("artists is: $artists")
            _artistList.postValue(artists)
        } catch (e: Exception) {
            Timber.e(e, "Service failure: $e")
        }
    }

}