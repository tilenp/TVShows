package com.example.tvshows.network.mapper

import com.example.tvshows.database.table.ShowSummary
import com.example.tvshows.network.remoteModel.RemoteShowSummary
import com.example.tvshows.network.remoteModel.RemoteWrapper
import com.example.tvshows.repository.paging.ShowSummariesWrapper

class ShowSummariesWrapperMapper constructor(
    private val showSummarySummaryMapper: Mapper<RemoteShowSummary, ShowSummary>
) : Mapper<RemoteWrapper<List<RemoteShowSummary>>, ShowSummariesWrapper> {

    override fun map(objectToMap: RemoteWrapper<List<RemoteShowSummary>>): ShowSummariesWrapper {
        return ShowSummariesWrapper(
            endOfPaginationReached = objectToMap.page == objectToMap.totalPages,
            showSummaries = objectToMap.results?.map { showSummarySummaryMapper.map(it) } ?: emptyList()
        )
    }
}