package com.example.tvshows.repository.service

import com.example.tvshows.database.model.ShowDetails
import io.reactivex.Single

interface ShowDetailsService {
    fun getShowDetails(showId: Int): Single<ShowDetails>
}