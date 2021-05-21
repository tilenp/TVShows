package com.example.tvshows.database.table

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.tvshows.database.model.ImagePath

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
    var id: Int = 0,
    var showId: Int = 0,
    var name: String = "",
    var seasonNumber: Int = 0,
    var imagePath: ImagePath? = null
)