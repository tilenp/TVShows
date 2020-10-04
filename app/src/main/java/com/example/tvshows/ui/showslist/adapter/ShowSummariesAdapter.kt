package com.example.tvshows.ui.showslist.adapter

import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.tvshows.database.table.ShowSummary
import com.example.tvshows.ui.showslist.viewholder.ShowSummaryViewHolder
import com.example.tvshows.ui.showslist.callback.OnShowClick

class ShowSummariesAdapter(
    private val onShowClick: OnShowClick
) : PagingDataAdapter<ShowSummary, RecyclerView.ViewHolder>(REPO_COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ShowSummaryViewHolder.create(parent = parent, onShowClick = onShowClick)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val repoItem = getItem(position)
        if (repoItem != null) {
            (holder as ShowSummaryViewHolder).bind(repoItem)
        }
    }

    companion object {
        private val REPO_COMPARATOR = object : DiffUtil.ItemCallback<ShowSummary>() {
            override fun areItemsTheSame(oldItem: ShowSummary, newItem: ShowSummary): Boolean =
                oldItem.showId == newItem.showId

            override fun areContentsTheSame(oldItem: ShowSummary, newItem: ShowSummary): Boolean =
                oldItem == newItem
        }
    }
}