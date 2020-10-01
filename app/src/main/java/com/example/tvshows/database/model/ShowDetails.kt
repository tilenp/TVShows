package com.example.tvshows.database.model

import androidx.room.Embedded
import androidx.room.Relation

data class ShowDetails(
    @Embedded val showContent: ShowContent,
    @Relation(
        parentColumn = "showId",
        entityColumn = "showId"
    )
    val genres: List<Genre>,
    @Relation(
        parentColumn = "showId",
        entityColumn = "showId"
    )
    val seasons: List<Season>
)