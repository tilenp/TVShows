package com.example.tvshows.database.dao

import androidx.room.*
import com.example.tvshows.database.table.Genre
import com.example.tvshows.database.table.Season
import com.example.tvshows.database.table.ShowContent
import com.example.tvshows.database.model.ShowDetails
import io.reactivex.Completable
import io.reactivex.Observable

@Dao
abstract class ShowDetailsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    protected abstract fun insertShowContent(showContent: ShowContent)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    protected abstract fun insertGenres(genres: List<Genre>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    protected abstract fun insertSeasons(seasons: List<Season>)

    @Transaction
    protected open fun insertShowDetailsInTransaction(showDetails: ShowDetails) {
        insertShowContent(showDetails.showContent)
        insertGenres(showDetails.genres)
        insertSeasons(showDetails.seasons)
    }

    fun insertShowDetails(showDetails: ShowDetails): Completable {
        return Completable.fromCallable { insertShowDetailsInTransaction(showDetails) }
    }

    @Transaction
    @Query("SELECT * FROM ShowContent WHERE ShowContent.showId = :showId")
    abstract fun getShowDetails(showId: Int): Observable<ShowDetails>
}