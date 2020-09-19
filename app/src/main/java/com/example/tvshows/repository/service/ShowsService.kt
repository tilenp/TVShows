package com.example.tvshows.repository.service

import com.example.tvshows.database.Show
import io.reactivex.Single

interface ShowsService {
    fun getMovies(page: Int): Single<List<Show>>
}