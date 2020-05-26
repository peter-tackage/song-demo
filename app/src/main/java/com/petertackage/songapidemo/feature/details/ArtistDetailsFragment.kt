package com.petertackage.songapidemo.feature.details

import android.os.Bundle
import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.petertackage.songapidemo.databinding.FragmentArtistDetailsBinding

class ArtistDetailsFragment : Fragment() {

    private object Flipper {
        const val LOADING = 0
        const val CONTENT = 1
        const val ERROR = 2
    }

    // ViewBinding using technique from https://developer.android.com/topic/libraries/view-binding
    private var _binding: FragmentArtistDetailsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentArtistDetailsBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        // FIXME Add graceful handling of missing parameters
        val artistName = arguments?.getString("artistNameId")!!

        val viewModel: ArtistDetailsFragmentViewModel by viewModels()
        viewModel.loadArtist(artistName)
        viewModel.state
            .observe(viewLifecycleOwner,
                Observer { state -> render(state) })
    }

    private fun render(state: ArtistDetailsState) {
        when (state) {
            ArtistDetailsState.IsLoading -> showLoading()
            is ArtistDetailsState.Loaded -> showContent(state)
            is ArtistDetailsState.Failed -> showError()
        }
    }

    private fun showLoading() {
        binding.viewFlipperArtistDetailsFlipper.displayedChild = Flipper.LOADING
    }

    private fun showContent(content: ArtistDetailsState.Loaded) {
        binding.viewFlipperArtistDetailsFlipper.displayedChild = Flipper.CONTENT
        renderContentHeader(content)
        renderTrackList(content)
    }

    private fun renderContentHeader(content: ArtistDetailsState.Loaded) {
        binding.contentArtistDetails.textViewArtistDetailsName.text = content.artist.name
        Glide.with(this).load(content.artist.backgroundUrl)
            .into(binding.contentArtistDetails.imageViewArtistDetailsBackground)
        Glide.with(this).load(content.artist.avatarUrl)
            .into(binding.contentArtistDetails.imageViewArtistDetailsAvatar)
    }

    private fun renderTrackList(content: ArtistDetailsState.Loaded) {
        val track = content.tracks.first()
        binding.contentArtistDetails.contentArtistDetailsTrack.textViewTrackListItemTitle.text =
            track.title
        binding.contentArtistDetails.contentArtistDetailsTrack.textViewTrackListItemDate.text =
            DateUtils.formatElapsedTime(track.duration)
        binding.contentArtistDetails.contentArtistDetailsTrack.textViewTrackListItemGenre.text =
            track.genre

        Glide.with(this).load(track.artworkUrl)
            .into(binding.contentArtistDetails.contentArtistDetailsTrack.imageViewTrackListItemAvatar)

    }

    private fun showError() {
        binding.viewFlipperArtistDetailsFlipper.displayedChild = Flipper.ERROR
    }

}
