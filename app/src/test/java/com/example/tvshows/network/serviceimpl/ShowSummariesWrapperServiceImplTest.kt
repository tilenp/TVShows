package com.example.tvshows.network.serviceimpl

import com.example.tvshows.network.api.ShowsApi
import com.example.tvshows.network.mapper.Mapper
import com.example.tvshows.network.remoteModel.RemoteShowSummary
import com.example.tvshows.network.remoteModel.RemoteWrapper
import com.example.tvshows.repository.paging.ShowSummariesWrapper
import com.example.tvshows.utilities.API_KEY
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.Single
import org.junit.Test

class ShowSummariesWrapperServiceImplTest {

    private val showsApi: ShowsApi = mock()
    private val mapper: Mapper<RemoteWrapper<List<RemoteShowSummary>>, ShowSummariesWrapper> = mock()
    private lateinit var showSummariesWrapperServiceImpl: ShowSummariesWrapperServiceImpl

    private val page = 1

    private fun setConditions(
        remoteWrapperSummary: RemoteWrapper<List<RemoteShowSummary>>,
        showWrapper: ShowSummariesWrapper
    ) {
        whenever(showsApi.getShowSummaries(API_KEY, page)).thenReturn(Single.just(remoteWrapperSummary))
        whenever(mapper.map(remoteWrapperSummary)).thenReturn(showWrapper)
        showSummariesWrapperServiceImpl = ShowSummariesWrapperServiceImpl(showsApi, mapper)
    }

    @Test
    fun service_returns_mapped_data() {
        // arrange
        val remoteWrapperSummary: RemoteWrapper<List<RemoteShowSummary>> = RemoteWrapper(totalPages = 0, page = 0, results = emptyList())
        val showWrapper = ShowSummariesWrapper(true, emptyList())
        setConditions(remoteWrapperSummary = remoteWrapperSummary, showWrapper = showWrapper)

        //act
        showSummariesWrapperServiceImpl.getShowSummariesWrapper(page)
            .test()
            .assertValue(showWrapper)
            .assertComplete()
    }
}