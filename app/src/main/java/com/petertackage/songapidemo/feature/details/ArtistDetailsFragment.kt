package com.petertackage.songapidemo.feature.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.petertackage.songapidemo.databinding.FragmentArtistDetailsBinding
import com.petertackage.songapidemo.feature.list.provideDiffItemCallback

class ArtistDetailsFragment : Fragment() {

    private object Flipper {
        const val LOADING = 0
        const val CONTENT = 1
        const val ERROR = 2
    }

    private lateinit var adapter: TrackRecyclerViewAdapter

    // ViewBinding using technique from https://developer.android.com/topic/libraries/view-binding
    private var _binding: FragmentArtistDetailsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ArtistDetailsFragmentViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentArtistDetailsBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerViewLayout()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        // FIXME Add graceful handling of missing parameters
        val artistName = extractArtistParam()

        adapter = TrackRecyclerViewAdapter(provideDiffItemCallback())
        binding.contentArtistDetails.recyclerViewArtistDetailsList.adapter = adapter

        viewModel.loadArtist(artistName)
        viewModel.state
            .observe(viewLifecycleOwner,
                Observer { state -> render(state) })
    }

    private fun extractArtistParam() = arguments?.getString("artistNameId")!!

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
        with(binding.contentArtistDetails) {
            textViewArtistDetailsName.text = content.artist.name
            Glide.with(this@ArtistDetailsFragment).load(content.artist.backgroundUrl)
                .into(imageViewArtistDetailsBackground)
            Glide.with(this@ArtistDetailsFragment).load(content.artist.avatarUrl)
                .into(imageViewArtistDetailsAvatar)
        }
    }

    private fun renderTrackList(content: ArtistDetailsState.Loaded) {
        adapter.submitList(content.tracks)
    }

    private fun showError() {
        binding.viewFlipperArtistDetailsFlipper.displayedChild = Flipper.ERROR
    }

    private fun initRecyclerViewLayout() {
        with(binding.contentArtistDetails.recyclerViewArtistDetailsList) {
            // FIXME Is this redundant? We set the LayoutManager in the XML too.
            layoutManager =
                androidx.recyclerview.widget.LinearLayoutManager(activity)
                    .apply { recycleChildrenOnDetach = true }
            addItemDecoration(
                androidx.recyclerview.widget.DividerItemDecoration(
                    context,
                    androidx.recyclerview.widget.RecyclerView.VERTICAL
                )
            )
        }
    }

}
