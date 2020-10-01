package com.example.tvshows.ui.showslist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.tvshows.R
import com.example.tvshows.database.table.Season
import com.example.tvshows.databinding.ViewHolderSeasonBinding
import com.example.tvshows.ui.showslist.callback.OnSeasonClick
import com.squareup.picasso.Picasso

class SeasonViewHolder(
    private val binding: ViewHolderSeasonBinding,
    private val onSeasonClick: OnSeasonClick
) : RecyclerView.ViewHolder(binding.root) {

    private lateinit var season: Season

    init {
        binding.root.setOnClickListener { onSeasonClick.onSeasonClick(season.name) }
    }

    fun bind(season: Season) {
        this.season = season
        with(binding) {
            Picasso
                .with(imageView.context)
                .load(season.imagePath?.medium)
                .into(imageView)
        }
    }

    companion object {
        fun create(parent: ViewGroup, onSeasonClick: OnSeasonClick): SeasonViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.view_holder_season, parent, false)
            val binding = ViewHolderSeasonBinding.bind(view)
            return SeasonViewHolder(binding, onSeasonClick)
        }
    }
}