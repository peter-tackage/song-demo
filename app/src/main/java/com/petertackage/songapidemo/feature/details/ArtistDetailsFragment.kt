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

        // FIXME Add graceful handling
        val artistName = arguments?.getString("artistNameId")!!

        val viewModel: ArtistDetailsFragmentViewModel by viewModels()
        viewModel.loadArtist(artistName)
        viewModel.artistDetailsState
            .observe(viewLifecycleOwner,
                Observer { artistDetailsState ->
                    binding.textViewArtistDetailsPlaceholder.text = artistDetailsState.toString()
                })
    }

}