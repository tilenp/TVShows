package com.example.tvshows.database.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.tvshows.database.model.ShowSummary
import io.reactivex.Observable


@Dao
interface ShowSummaryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertShowSummaries(showSummaries: List<ShowSummary>)

    @Query("SELECT * FROM ShowSummary ORDER BY id ASC")
    fun getShowSummaries(): PagingSource<Int, ShowSummary>

    @Query("SELECT * FROM ShowSummary WHERE ShowSummary.showId = :showId")
    fun getShowSummary(showId: Int): Observable<ShowSummary>

    @Query("DELETE FROM ShowSummary")
    fun deleteSummaries()
}