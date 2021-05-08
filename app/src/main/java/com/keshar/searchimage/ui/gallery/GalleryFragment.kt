package com.keshar.searchimage.ui.gallery

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import com.keshar.searchimage.R
import com.keshar.searchimage.databinding.FragmentGalleryBinding
import com.keshar.searchimage.models.UnsplashPhoto
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GalleryFragment : Fragment(R.layout.fragment_gallery),
    UnsplashPhotoAdapter.onItemClickListerner {

    private val viewModel by viewModels<GalleryViewModel>()
    private var _bingding: FragmentGalleryBinding? = null
    private val binding get() = _bingding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _bingding = FragmentGalleryBinding.bind(view)

        val adapter = UnsplashPhotoAdapter(this)
        binding.apply {
            unsplashItemRecyclerView.setHasFixedSize(true)
            unsplashItemRecyclerView.itemAnimator = null
            unsplashItemRecyclerView.adapter = adapter.withLoadStateHeaderAndFooter(
                header = UnsplashStateLoadAdapter { adapter.retry() },
                footer = UnsplashStateLoadAdapter { adapter.retry() }
            )
            retryBtn.setOnClickListener {
                adapter.retry()
            }

        }

        viewModel.photos.observe(viewLifecycleOwner) {
            adapter.submitData(viewLifecycleOwner.lifecycle, it)
        }

        setHasOptionsMenu(true)

        adapter.addLoadStateListener { loadState ->
            binding.apply {
                progressBar.isVisible = loadState.source.refresh is LoadState.Loading
                unsplashItemRecyclerView.isVisible =
                    loadState.source.refresh is LoadState.NotLoading
                retryBtn.isVisible = loadState.source.refresh is LoadState.Error
                resultError.isVisible = loadState.source.refresh is LoadState.Error

                if (loadState.source.refresh is LoadState.NotLoading &&
                    loadState.append.endOfPaginationReached &&
                    adapter.itemCount < 1
                ) {
                    unsplashItemRecyclerView.isVisible = false
                    resultNotFound.isVisible = true

                } else {
                    resultNotFound.isVisible = false
                }
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _bingding = null
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.gallery_menu, menu)

        val searchItem = menu.findItem(R.id.search)
        val searchView = searchItem.actionView as SearchView

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) {
                    binding.unsplashItemRecyclerView.scrollToPosition(0)
                    viewModel.sarchPhotos(query)
                    searchView.clearFocus()
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }

        })

    }

    override fun onItemClicked(photo: UnsplashPhoto) {
        val action = GalleryFragmentDirections.actionGalleryFragmentToDetailsFragment(photo)
        findNavController().navigate(action)
    }


}