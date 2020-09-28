package com.example.tvshows.network.serviceimpl

import com.example.tvshows.network.api.ShowsApi
import com.example.tvshows.network.mapper.Mapper
import com.example.tvshows.network.remoteModel.RemoteWrapper
import com.example.tvshows.network.remoteModel.RemoteShowSummary
import com.example.tvshows.repository.paging.ShowSummariesWrapper
import com.example.tvshows.repository.service.ShowSummariesWrapperService
import io.reactivex.Single
import javax.inject.Inject

class ShowSummariesWrapperServiceImpl @Inject constructor(
    private val showsApi: ShowsApi,
    private val mapper: Mapper<RemoteWrapper<List<RemoteShowSummary>>, ShowSummariesWrapper>
) : ShowSummariesWrapperService {

    override fun getShowSummariesWrapper(page: Int): Single<ShowSummariesWrapper> {
        return showsApi.getShowSummaries(page = page)
            .map { response -> mapper.map(response) }
    }
}