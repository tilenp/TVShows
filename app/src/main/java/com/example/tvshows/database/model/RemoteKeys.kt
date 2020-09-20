package com.example.tvshows.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class RemoteKeys(
    @PrimaryKey
    val showId: Long,
    val prevKey: Int?,
    val nextKey: Int?
)