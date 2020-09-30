package com.example.tvshows.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.tvshows.database.model.ShowDetails
import io.reactivex.Completable
import io.reactivex.Observable

@Dao
interface ShowDetailsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertShowDetails(showDetails: ShowDetails)

    @Query("SELECT * FROM ShowDetails WHERE ShowDetails.showId = :showId")
    fun getShowDetails(showId: Int): Observable<ShowDetails>
}