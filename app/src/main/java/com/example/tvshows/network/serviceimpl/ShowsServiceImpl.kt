package com.example.tvshows.network.serviceimpl

import com.example.tvshows.network.api.ShowsApi
import com.example.tvshows.network.mapper.Mapper
import com.example.tvshows.network.remoteModel.RemoteWrapper
import com.example.tvshows.network.remoteModel.RemoteShow
import com.example.tvshows.repository.paging.ShowsWrapper
import com.example.tvshows.repository.service.ShowsService
import io.reactivex.Single
import javax.inject.Inject

class ShowsServiceImpl @Inject constructor(
    private val showsApi: ShowsApi,
    private val mapper: Mapper<RemoteWrapper<List<RemoteShow>>, ShowsWrapper>
) : ShowsService {

    override fun getShows(page: Int): Single<ShowsWrapper> {
        return showsApi.getShows(page = page)
            .map { response -> mapper.map(response) }
    }
}