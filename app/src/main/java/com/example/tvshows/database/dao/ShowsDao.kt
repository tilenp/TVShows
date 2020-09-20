package com.example.tvshows.database.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.tvshows.database.model.Show


@Dao
interface ShowsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertShows(shows: List<Show>)

    @Query("SELECT * FROM Show ORDER BY id ASC")
    fun getShows(): PagingSource<Int, Show>

    @Query("DELETE FROM Show")
    fun deleteShows()
}