package com.example.tvshows.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Show(
    @PrimaryKey
    var id: Long,
    var imagePath: ImagePath?,
    var rating: Float,
    var summary: String
)