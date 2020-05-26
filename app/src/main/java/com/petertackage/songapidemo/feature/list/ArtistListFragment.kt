package com.petertackage.songapidemo.feature.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.petertackage.songapidemo.databinding.FragmentListBinding
import com.petertackage.songapidemo.feature.list.ArtistListFragment.Flipper.CONTENT
import com.petertackage.songapidemo.feature.list.ArtistListFragment.Flipper.ERROR
import com.petertackage.songapidemo.feature.list.ArtistListFragment.Flipper.LOADING

class ArtistListFragment : Fragment() {

    private lateinit var adapter: ArtistRecyclerViewAdapter

    private object Flipper {
        const val LOADING = 0
        const val CONTENT = 1
        const val ERROR = 2
    }

    // ViewBinding using technique from https://developer.android.com/topic/libraries/view-binding
    private var _binding: FragmentListBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentListBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        adapter = ArtistRecyclerViewAdapter(provideDiffItemCallback())
        binding.contentArtistList.recyclerViewArtistListList.adapter = adapter

        val viewModel: ArtistListFragmentViewModel by viewModels()
        viewModel.state
            .observe(viewLifecycleOwner,
                Observer { state -> render(state) })
    }

    private fun render(state: ArtistListState) {
        when (state) {
            ArtistListState.IsLoading -> showLoading()
            is ArtistListState.Loaded -> showContent(state)
            is ArtistListState.Failed -> showError()
        }
    }

    private fun showLoading() {
        binding.viewFlipperArtistListFlipper.displayedChild = LOADING
    }

    private fun showContent(content: ArtistListState.Loaded) {
        binding.viewFlipperArtistListFlipper.displayedChild = CONTENT
        adapter.submitList(content.artists)
    }

    private fun showError() {
        binding.viewFlipperArtistListFlipper.displayedChild = ERROR
    }

    private fun initRecyclerView() {
        with(binding.contentArtistList.recyclerViewArtistListList) {
            // FIXME Is this redundant? We set the LayoutManager in the XML too.
            layoutManager =
                LinearLayoutManager(activity).apply { recycleChildrenOnDetach = true }
            addItemDecoration(DividerItemDecoration(context, RecyclerView.VERTICAL))
        }
    }
}