package com.example.tvshows.ui.showslist.viewholder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.recyclerview.widget.RecyclerView
import com.example.tvshows.R
import com.example.tvshows.databinding.ViewHolderLoadingBinding
import com.example.tvshows.ui.showslist.callback.OnRetryClick

class LoadingViewHolder(
    private val binding: ViewHolderLoadingBinding,
    private val onRetryClick: OnRetryClick
) : RecyclerView.ViewHolder(binding.root) {

    init {
        binding.retryButton.setOnClickListener { onRetryClick.retry() }
    }

    fun bind(loadState: LoadState) {
        with(binding) {
            progressBar.isVisible = loadState is LoadState.Loading
            retryButton.isVisible = loadState !is LoadState.Loading
        }
    }

    companion object {
        fun create(parent: ViewGroup, onRetryClick: OnRetryClick): LoadingViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.view_holder_loading, parent, false)
            val binding = ViewHolderLoadingBinding.bind(view)
            return LoadingViewHolder(binding = binding, onRetryClick = onRetryClick)
        }
    }
}