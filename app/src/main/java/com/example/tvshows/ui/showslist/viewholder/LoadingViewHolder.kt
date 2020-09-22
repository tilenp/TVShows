package com.example.tvshows.ui.showslist.viewholder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.recyclerview.widget.RecyclerView
import com.example.tvshows.R
import com.example.tvshows.databinding.ViewHolderLoadingBinding

class LoadingViewHolder(
    private val binding: ViewHolderLoadingBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(loadState: LoadState) {
        binding.progressBar.isVisible = loadState is LoadState.Loading
    }

    companion object {
        fun create(parent: ViewGroup): LoadingViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.view_holder_loading, parent, false)
            val binding = ViewHolderLoadingBinding.bind(view)
            return LoadingViewHolder(
                binding
            )
        }
    }
}