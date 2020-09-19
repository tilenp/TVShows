package com.example.tvshows.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Show(
    @PrimaryKey
    var id: Int,
    var image: String,
    var rating: Float,
    var summary: String
)