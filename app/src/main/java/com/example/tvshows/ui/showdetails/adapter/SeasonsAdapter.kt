package com.example.tvshows.ui.showdetails.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.tvshows.database.table.Season
import com.example.tvshows.ui.showdetails.callback.OnSeasonClick
import com.example.tvshows.ui.showdetails.viewholder.SeasonViewHolder

class SeasonsAdapter(
    private val onSeasonClick: OnSeasonClick
) : RecyclerView.Adapter<SeasonViewHolder>() {

    private var seasons: List<Season> = emptyList()

    fun setSeasons(seasons: List<Season>) {
        this.seasons = seasons
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SeasonViewHolder {
        return SeasonViewHolder.create(parent = parent, onSeasonClick = onSeasonClick)
    }

    override fun onBindViewHolder(holder: SeasonViewHolder, position: Int) {
        holder.bind(seasons[position])
    }

    override fun getItemCount() = seasons.size
}
