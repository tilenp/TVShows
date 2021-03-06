package com.example.tvshows.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.tvshows.database.table.PagingKeys

@Dao
interface PagingKeysDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPagingKeys(pagingKeys: List<PagingKeys>)

    @Query("SELECT * FROM PagingKeys WHERE PagingKeys.elementId = :elementId")
    fun getPagingKeysForElementId(elementId: Int): PagingKeys?

    @Query("DELETE FROM PagingKeys")
    fun deletePagingKeys()
}