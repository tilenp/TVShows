package com.example.tvshows.database.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    foreignKeys = [ForeignKey(
        entity = ShowSummary::class,
        parentColumns = ["showId"],
        childColumns = ["showId"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index(value = ["showId"])]
)
data class Season(
    @PrimaryKey
    var id: Int,
    var showId: Int = 0,
    var name: String,
    var seasonNumber: Int,
    var imagePath: ImagePath?
)