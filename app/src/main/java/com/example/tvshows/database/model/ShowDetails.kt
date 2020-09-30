package com.example.tvshows.database.model

import androidx.room.*

@Entity(
    foreignKeys = [ForeignKey(
        entity = ShowSummary::class,
        parentColumns = ["showId"],
        childColumns = ["showId"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class  ShowDetails @JvmOverloads constructor(
    @PrimaryKey
    var showId: Int,
    @Ignore var genres: List<Genre> = emptyList(),
    var summary: String,
    @Ignore var seasons: List<Season> = emptyList()
)