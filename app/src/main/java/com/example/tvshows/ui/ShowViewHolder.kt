package com.example.tvshows.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.tvshows.R
import com.example.tvshows.database.model.Show
import com.example.tvshows.databinding.ViewHolderShowBinding

class ShowViewHolder(
    private val binding: ViewHolderShowBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(show: Show) {
        binding.showImageUrl.text = show.image
    }

    companion object {
        fun create(parent: ViewGroup): ShowViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.view_holder_show, parent, false)
            val binding = ViewHolderShowBinding.bind(view)
            return ShowViewHolder(binding)
        }
    }
}