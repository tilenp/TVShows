package com.example.tvshows.repository.service

import com.example.tvshows.database.model.Show
import io.reactivex.Single

interface ShowsService {
    fun getShows(page: Int): Single<List<Show>>
}