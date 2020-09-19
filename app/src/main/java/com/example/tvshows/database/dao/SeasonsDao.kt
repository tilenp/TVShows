package com.example.tvshows.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.tvshows.database.model.Season
import io.reactivex.Single

@Dao
interface SeasonsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(repos: List<Season>)

    @Query("SELECT * " +
            "FROM Season " +
            "WHERE Season.showId = :showId " +
            "ORDER BY Season.seasonNumber ASC")
    fun getSeasons(showId: Int): Single<List<Season>>
}