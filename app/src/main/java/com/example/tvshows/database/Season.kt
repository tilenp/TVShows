package com.example.tvshows.database

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    foreignKeys = [ForeignKey(
        entity = Show::class,
        parentColumns = ["id"],
        childColumns = ["showId"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index("showId")]
)
data class Season(
    @PrimaryKey
    var id: Int,
    var showId: String,
    var seasonNumber: Int,
    var image: Int,
    var numberOfEpisodes: Int
)