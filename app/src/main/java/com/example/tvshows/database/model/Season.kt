package com.example.tvshows.database.model

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
    var showId: Int,
    var seasonNumber: Int,
    var image: Int,
    var numberOfEpisodes: Int
)