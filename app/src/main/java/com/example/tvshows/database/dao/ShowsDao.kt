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
    fun insertAll(repos: List<Show>)

    @Query("SELECT * FROM Show")
    fun getShows(): PagingSource<Int, Show>
}