package com.example.tvshows.ui.showslist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.tvshows.R
import com.example.tvshows.database.table.ShowSummary
import com.example.tvshows.databinding.ViewHolderShowBinding
import com.example.tvshows.ui.showslist.callback.OnShowClick
import com.squareup.picasso.Picasso

class ShowSummaryViewHolder(
    private val binding: ViewHolderShowBinding,
    private val onShowClick: OnShowClick
) : RecyclerView.ViewHolder(binding.root) {

    private lateinit var showSummary: ShowSummary

    init {
        binding.root.setOnClickListener { onShowClick.showClicked(showSummary.showId) }
    }

    fun bind(showSummary: ShowSummary) {
        this.showSummary = showSummary
        with(binding) {
            Picasso
                .with(imageView.context)
                .load(showSummary.imagePath?.medium)
                .into(imageView)
            textView.text = showSummary.name
        }
    }

    companion object {
        fun create(parent: ViewGroup, onShowClick: OnShowClick): ShowSummaryViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.view_holder_show, parent, false)
            val binding = ViewHolderShowBinding.bind(view)
            return ShowSummaryViewHolder(binding = binding, onShowClick = onShowClick)
        }
    }
}