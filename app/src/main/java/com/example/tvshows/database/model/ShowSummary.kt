package com.example.tvshows.database.model

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(indices = [Index(value = ["showId"], unique = true)])
data class ShowSummary(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    var showId: Int,
    var name: String,
    var imagePath: ImagePath?
)