package com.example.tvshows.repository

import com.example.tvshows.database.TVShowsDatabase
import com.example.tvshows.database.model.Genre
import com.example.tvshows.database.model.ShowDetails
import com.example.tvshows.database.model.ShowSummary
import com.example.tvshows.repository.service.ShowDetailsService
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.functions.BiFunction
import javax.inject.Inject

class ShowDetailsRepository @Inject constructor(
    private val showDetailsService: ShowDetailsService,
    private val database: TVShowsDatabase
) {

    fun updateShowDetails(showId: Int): Completable {
        return showDetailsService.getShowDetails(showId = showId)
            .flatMapCompletable { showDetails -> updateDatabase(showDetails) }
    }

    private fun updateDatabase(showDetails: ShowDetails): Completable {
        return Completable.fromAction {
            with(database) {
                runInTransaction {
                    getGenreDao().insertGenres(showDetails.genres)
                    getShowDetailsDao().insertShowDetails(showDetails)
                }
            }
        }
    }

    fun getShowSummary(showId: Int): Observable<ShowSummary> {
        return database.getShowSummaryDao().getShowSummary(showId = showId)
    }

    fun getShowDetails(showId: Int): Observable<ShowDetails> {
        return Observable.combineLatest(
            database.getShowDetailsDao().getShowDetails(showId),
            database.getGenreDao().getGenres(showId),
            BiFunction { showDetails: ShowDetails, genres: List<Genre> ->
                showDetails.also { it.genres = genres }
            }
        )
    }
}