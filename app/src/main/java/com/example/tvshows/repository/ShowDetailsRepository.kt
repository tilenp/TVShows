package com.example.tvshows.repository

import com.example.tvshows.database.dao.ShowDetailsDao
import com.example.tvshows.database.dao.ShowSummaryDao
import com.example.tvshows.database.model.ShowDetails
import com.example.tvshows.database.table.Season
import com.example.tvshows.database.table.ShowSummary
import com.example.tvshows.repository.service.ShowDetailsService
import com.example.tvshows.utilities.SEASON_NUMBER_COMPARATOR
import io.reactivex.Completable
import io.reactivex.Observable
import java.util.*
import javax.inject.Inject
import javax.inject.Named

class ShowDetailsRepository @Inject constructor(
    private val showDetailsService: ShowDetailsService,
    private val showSummaryDao: ShowSummaryDao,
    private val showDetailsDao: ShowDetailsDao,
    @Named(SEASON_NUMBER_COMPARATOR) private val seasonComparator: Comparator<Season?>
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
            .map { showDetails ->
                Collections.sort(showDetails.seasons, seasonComparator)
                showDetails
            }
    }
}