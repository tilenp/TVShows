package com.example.tvshows.network.serviceimpl

import com.example.tvshows.database.model.ShowDetails
import com.example.tvshows.network.api.ShowsApi
import com.example.tvshows.network.mapper.Mapper
import com.example.tvshows.network.remoteModel.RemoteShowDetails
import com.example.tvshows.utilities.API_KEY
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.Single
import org.junit.Test

class ShowDetailsServiceImplTest {

    private val showsApi: ShowsApi = mock()
    private val mapper: Mapper<RemoteShowDetails, ShowDetails> = mock()
    private lateinit var showDetailsServiceImpl: ShowDetailsServiceImpl

    private val showId = 1

    private fun setConditions(
        remoteShowDetails: RemoteShowDetails,
        showDetails: ShowDetails
    ) {
        whenever(showsApi.getShowDetails(showId = showId, apiKey = API_KEY)).thenReturn(Single.just(remoteShowDetails))
        whenever(mapper.map(any())).thenReturn(showDetails)
        showDetailsServiceImpl = ShowDetailsServiceImpl(showsApi, mapper)
    }

    @Test
    fun service_returns_mapped_data() {
        // arrange
        val remoteShowDetails: RemoteShowDetails = mock()
        val showDetails: ShowDetails = mock()
        setConditions(remoteShowDetails = remoteShowDetails, showDetails = showDetails)

        //act
        showDetailsServiceImpl.getShowDetails(showId = showId)
            .test()
            .assertValue(showDetails)
            .assertComplete()
            .dispose()
    }
}