package com.example.tvshows.repository.service

import com.example.tvshows.repository.paging.ShowsWrapper
import io.reactivex.Single

interface ShowsService {
    fun getShows(page: Int): Single<ShowsWrapper>
}