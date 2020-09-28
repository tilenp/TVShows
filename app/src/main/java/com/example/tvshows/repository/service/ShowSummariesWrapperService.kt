package com.example.tvshows.repository.service

import com.example.tvshows.repository.paging.ShowSummariesWrapper
import io.reactivex.Single

interface ShowSummariesWrapperService {
    fun getShowSummariesWrapper(page: Int): Single<ShowSummariesWrapper>
}