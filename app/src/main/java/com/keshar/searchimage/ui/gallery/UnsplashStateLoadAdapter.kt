package com.keshar.searchimage.ui.gallery

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.keshar.searchimage.databinding.UnsplashStateLoadLayoutBinding


class UnsplashStateLoadAdapter(private val retry: () -> Unit) :
    LoadStateAdapter<UnsplashStateLoadAdapter.UnsplashLoadStateViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        loadState: LoadState
    ): UnsplashLoadStateViewHolder {
        val binding = UnsplashStateLoadLayoutBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return UnsplashLoadStateViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UnsplashLoadStateViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    inner class UnsplashLoadStateViewHolder(private val binding: UnsplashStateLoadLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.retryBtn.setOnClickListener {
                retry.invoke()
            }
        }

        fun bind(loadState: LoadState) {
            binding.apply {
                progressBar.isVisible = loadState is LoadState.Loading
                loadErrTxtv.isVisible = loadState !is LoadState.Loading
                retryBtn.isVisible = loadState !is LoadState.Loading
            }
        }

    }


}