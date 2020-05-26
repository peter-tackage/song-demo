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

class ArtistListFragment : Fragment() {

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

        val adapter = ArtistRecyclerViewAdapter(provideDiffItemCallback())
        binding.list.adapter = adapter

        val viewModel: ArtistListFragmentViewModel by viewModels()
        viewModel.artistList
            .observe(viewLifecycleOwner,
                Observer { artists -> adapter.submitList(artists) })
    }

    private fun initRecyclerView() {
        with(binding.list) {
            layoutManager =
                LinearLayoutManager(activity).apply { recycleChildrenOnDetach = true }
            addItemDecoration(DividerItemDecoration(context, RecyclerView.VERTICAL))
        }
    }
}