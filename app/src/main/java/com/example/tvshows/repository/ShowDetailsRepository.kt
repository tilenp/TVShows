package com.example.tvshows.repository

import com.example.tvshows.database.dao.ShowDetailsDao
import com.example.tvshows.database.dao.ShowSummaryDao
import com.example.tvshows.database.model.ShowDetails
import com.example.tvshows.database.model.ShowSummary
import com.example.tvshows.repository.service.ShowDetailsService
import io.reactivex.Completable
import io.reactivex.Observable
import javax.inject.Inject

class ShowDetailsRepository @Inject constructor(
    private val showSummaryDao: ShowSummaryDao,
    private val showDetailsDao: ShowDetailsDao,
    private val showDetailsService: ShowDetailsService
) {

    fun updateShowDetails(showId: Int): Completable {
        return showDetailsService.getShowDetails(showId = showId)
            .flatMapCompletable { showDetails -> showDetailsDao.insertShowDetails(showDetails) }
    }

    fun getShowSummary(showId: Int): Observable<ShowSummary> {
        return showSummaryDao.getShowSummary(showId = showId)
    }

    fun getShowDetails(showId: Int): Observable<ShowDetails> {
        return showDetailsDao.getShowDetails(showId = showId)
    }
}