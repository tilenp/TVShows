package com.example.tvshows.ui.showslist.adapter

import android.view.ViewGroup
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import com.example.tvshows.ui.showslist.callback.OnRetryClick
import com.example.tvshows.ui.showslist.viewholder.LoadingViewHolder

class LoadingAdapter(
    private val onRetryClick: OnRetryClick
) : LoadStateAdapter<LoadingViewHolder>() {

    override fun onBindViewHolder(holder: LoadingViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): LoadingViewHolder {
        return LoadingViewHolder.create(parent = parent, onRetryClick = onRetryClick)
    }
}