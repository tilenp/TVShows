package com.example.tvshows.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class PagingKeys(
    @PrimaryKey
    val elementId: Int,
    val prevKey: Int?,
    val nextKey: Int?
)