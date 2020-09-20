package com.example.tvshows.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Show(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    var showId: Long,
    var imagePath: ImagePath?,
    var rating: Float,
    var summary: String
)