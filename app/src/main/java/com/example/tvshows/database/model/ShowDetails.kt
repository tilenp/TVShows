package com.example.tvshows.database.model

import androidx.room.Embedded
import androidx.room.Relation
import com.example.tvshows.database.table.Genre
import com.example.tvshows.database.table.Season
import com.example.tvshows.database.table.ShowContent

data class ShowDetails(
    @Embedded val showContent: ShowContent = ShowContent(),
    @Relation(
        parentColumn = "showId",
        entityColumn = "showId"
    )
    val genres: List<Genre> = emptyList(),
    @Relation(
        parentColumn = "showId",
        entityColumn = "showId"
    )
    val seasons: List<Season> = emptyList()
)