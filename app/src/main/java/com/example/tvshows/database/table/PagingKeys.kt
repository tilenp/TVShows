package com.example.tvshows.database.table

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class PagingKeys(
    @PrimaryKey
    val elementId: Int = 0,
    val prevKey: Int? = 0,
    val nextKey: Int? = 0
)