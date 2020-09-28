package com.example.tvshows.network.mapper

import com.example.tvshows.database.model.ShowSummary
import com.example.tvshows.network.remoteModel.RemoteWrapper
import com.example.tvshows.network.remoteModel.RemoteShowSummary
import com.example.tvshows.repository.paging.ShowSummariesWrapper
import javax.inject.Inject

class ShowSummariesWrapperMapper @Inject constructor(
    private val showSummarySummaryMapper: Mapper<RemoteShowSummary, ShowSummary>
) : Mapper<RemoteWrapper<List<RemoteShowSummary>>, ShowSummariesWrapper> {

    override fun map(objectToMap: RemoteWrapper<List<RemoteShowSummary>>): ShowSummariesWrapper {
        return ShowSummariesWrapper(
            endOfPaginationReached = objectToMap.page == objectToMap.totalPages,
            showSummaries = objectToMap.results?.map { showSummarySummaryMapper.map(it) } ?: emptyList()
        )
    }
}