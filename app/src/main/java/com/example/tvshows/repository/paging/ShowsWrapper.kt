package com.example.tvshows.repository.paging

import com.example.tvshows.database.model.Show

data class ShowsWrapper(
    val endOfPaginationReached: Boolean,
    val shows: List<Show>
)