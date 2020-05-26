package com.petertackage.songapidemo.feature.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
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
        binding.viewFlipperArtistListFlipper.displayedChild = Flipper.LOADING
    }

    private fun showContent(content: ArtistDetailsState.Loaded) {
        binding.viewFlipperArtistListFlipper.displayedChild = Flipper.CONTENT
        binding.contentArtistDetails.textViewArtistDetailsPlaceholder.text = content.toString()
    }

    private fun showError() {
        binding.viewFlipperArtistListFlipper.displayedChild = Flipper.ERROR
    }

}
