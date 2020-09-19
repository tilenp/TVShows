package com.example.tvshows.database.dao

import androidx.paging.PagingSource
import androidx.room.*
import com.example.tvshows.database.model.RemoteKeys
import com.example.tvshows.database.model.Show


@Dao
abstract class ShowsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertShows(shows: List<Show>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertRemoteKeys(remoteKeys: List<RemoteKeys>)

    @Transaction
    open fun insertShowsAndRemoteKeys(
        clearShows: Boolean,
        remoteKeys: List<RemoteKeys>,
        shows: List<Show>
    ) {
        if (clearShows) {
            deleteRemoteKeys()
            deleteShows()
        }
        insertRemoteKeys(remoteKeys)
        insertShows(shows)
    }

    @Query("SELECT * FROM Show")
    abstract fun getShows(): PagingSource<Int, Show>

    @Query("SELECT * FROM RemoteKeys WHERE RemoteKeys.showId = :showId")
    abstract fun getRemoteKeysForShowId(showId: Int): RemoteKeys?

    @Query("DELETE FROM Show")
    abstract fun deleteShows()

    @Query("DELETE FROM RemoteKeys")
    abstract fun deleteRemoteKeys()
}