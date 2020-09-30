package com.example.tvshows.network.serviceimpl

import com.example.tvshows.database.model.ShowDetails
import com.example.tvshows.network.api.ShowsApi
import com.example.tvshows.network.mapper.Mapper
import com.example.tvshows.network.remoteModel.RemoteShowDetails
import com.example.tvshows.repository.service.ShowDetailsService
import io.reactivex.Single
import javax.inject.Inject

class ShowDetailsServiceImpl @Inject constructor(
    private val showsApi: ShowsApi,
    private val mapper: Mapper<RemoteShowDetails, ShowDetails>
): ShowDetailsService {

    override fun getShowDetails(showId: Int): Single<ShowDetails> {
        return showsApi.getShowDetails(showId = showId)
            .map { response -> mapper.map(response) }
    }
}