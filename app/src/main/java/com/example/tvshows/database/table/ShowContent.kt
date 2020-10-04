package com.example.tvshows.database.table

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    foreignKeys = [ForeignKey(
        entity = ShowSummary::class,
        parentColumns = ["showId"],
        childColumns = ["showId"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class ShowContent(
    @PrimaryKey
    var showId: Int = 0,
    var summary: String = ""
)