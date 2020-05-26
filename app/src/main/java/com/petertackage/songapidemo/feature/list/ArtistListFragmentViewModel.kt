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

class ArtistListFragmentViewModel(
    private val artistService: ArtistService = provideArtistService(),
    dispatcherProvider: CoroutineDispatcherProvider = CoroutineDispatcherProvider()
) : ViewModel() {

    private val _artistList = MutableLiveData<ArtistListState>()
    val artistList: LiveData<ArtistListState> get() = _artistList

    init {
        viewModelScope.launch(dispatcherProvider.io) { fetchTopArtists() }
    }

    private suspend fun fetchTopArtists() {
        emit(ArtistListState.IsLoading)
        Result.runCatching { artistService.topArtists() }
            .onSuccess { emit(ArtistListState.Loaded(it)) }
            .onFailure { emit(ArtistListState.Failed(it)) }
    }

    private fun emit(state: ArtistListState) {
        _artistList.postValue(state)
    }

}

sealed class ArtistListState {
    object IsLoading : ArtistListState()
    data class Loaded(val artists: List<Artist>) : ArtistListState()
    data class Failed(val throwable: Throwable) : ArtistListState()
}