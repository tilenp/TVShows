package com.example.tvshows.repository.paging

import com.example.tvshows.database.table.ShowSummary

data class ShowSummariesWrapper(
    val endOfPaginationReached: Boolean,
    val showSummaries: List<ShowSummary>
)