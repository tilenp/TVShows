package com.example.tvshows.database.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.tvshows.database.model.ShowSummary


@Dao
interface ShowSummaryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertShowSummaries(showSummaries: List<ShowSummary>)

    @Query("SELECT * FROM ShowSummary ORDER BY id ASC")
    fun getShowSummaries(): PagingSource<Int, ShowSummary>

    @Query("DELETE FROM ShowSummary")
    fun deleteSummaries()
}