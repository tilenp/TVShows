package com.example.tvshows.network.serviceimpl

import com.example.tvshows.network.api.ShowsApi
import com.example.tvshows.network.mapper.Mapper
import com.example.tvshows.network.remoteModel.RemoteShow
import com.example.tvshows.network.remoteModel.RemoteWrapper
import com.example.tvshows.repository.paging.ShowsWrapper
import com.example.tvshows.utilities.API_KEY
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.Single
import org.junit.Test

class ShowsServiceImplTest {

    private val showsApi: ShowsApi = mock()
    private val mapper: Mapper<RemoteWrapper<List<RemoteShow>>, ShowsWrapper> = mock()
    private lateinit var showsServiceImpl: ShowsServiceImpl

    private val page = 1

    private fun setConditions(
        remoteWrapper: RemoteWrapper<List<RemoteShow>>,
        showWrapper: ShowsWrapper
    ) {
        whenever(showsApi.getShows(API_KEY, page)).thenReturn(Single.just(remoteWrapper))
        whenever(mapper.map(remoteWrapper)).thenReturn(showWrapper)
        showsServiceImpl = ShowsServiceImpl(showsApi, mapper)
    }

    @Test
    fun service_returns_mapped_data() {
        // arrange
        val remoteWrapper: RemoteWrapper<List<RemoteShow>> = RemoteWrapper(totalPages = 0, page = 0, results = emptyList())
        val showWrapper = ShowsWrapper(true, emptyList())
        setConditions(remoteWrapper = remoteWrapper, showWrapper = showWrapper)

        //act
        showsServiceImpl.getShows(page)
            .test()
            .assertValue(showWrapper)
            .assertComplete()
    }
}