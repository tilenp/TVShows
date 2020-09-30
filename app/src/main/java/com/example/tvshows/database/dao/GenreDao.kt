package com.example.tvshows.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.tvshows.database.model.Genre
import io.reactivex.Observable

@Dao
interface GenreDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertGenres(genres: List<Genre>)

    @Query("SELECT * FROM Genre WHERE Genre.showId = :showId")
    fun getGenres(showId: Int): Observable<List<Genre>>
}