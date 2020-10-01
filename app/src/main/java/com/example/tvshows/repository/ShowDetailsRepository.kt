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
    private val showDetailsService: ShowDetailsService,
    private val showSummaryDao: ShowSummaryDao,
    private val showDetailsDao: ShowDetailsDao
) {

    fun updateShowDetails(showId: Int): Completable {
        return showDetailsService.getShowDetails(showId)
            .flatMapCompletable { showDetails -> showDetailsDao.insertShowDetails(showDetails) }
    }

    fun getShowSummary(showId: Int): Observable<ShowSummary> {
        return showSummaryDao.getShowSummary(showId)
    }

    fun getShowDetails(showId: Int): Observable<ShowDetails> {
        return showDetailsDao.getShowDetails(showId)
    }
}