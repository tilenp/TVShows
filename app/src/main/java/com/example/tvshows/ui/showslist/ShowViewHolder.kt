package com.example.tvshows.ui.showslist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.tvshows.R
import com.example.tvshows.database.model.Show
import com.example.tvshows.databinding.ViewHolderShowBinding
import com.example.tvshows.ui.showslist.callback.OnShowClick
import com.squareup.picasso.Picasso

class ShowViewHolder(
    private val binding: ViewHolderShowBinding,
    private val onShowClick: OnShowClick
) : RecyclerView.ViewHolder(binding.root) {

    private lateinit var show: Show

    init {
        binding.root.setOnClickListener { onShowClick.showClicked(showId = show.showId) }
    }

    fun bind(show: Show) {
        this.show = show
        with(binding) {
            Picasso
                .with(imageView.context)
                .load(show.imagePath?.medium)
                .into(imageView)
            textView.text = show.name
        }
    }

    companion object {
        fun create(parent: ViewGroup, onShowClick: OnShowClick): ShowViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.view_holder_show, parent, false)
            val binding = ViewHolderShowBinding.bind(view)
            return ShowViewHolder(binding = binding, onShowClick = onShowClick)
        }
    }
}