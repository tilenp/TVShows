package com.example.tvshows.network.serviceimpl

import com.example.tvshows.database.model.Show
import com.example.tvshows.network.api.ShowsApi
import com.example.tvshows.network.mapper.Mapper
import com.example.tvshows.network.remoteModel.RemoteShow
import com.example.tvshows.repository.service.ShowsService
import io.reactivex.Single
import javax.inject.Inject

class ShowsServiceImpl @Inject constructor(
    private val showsApi: ShowsApi,
    private val mapper: Mapper<RemoteShow, Show>
): ShowsService {

    override fun getMovies(page: Int): Single<List<Show>> {
        return showsApi.getShows(page)
            .map { shows -> shows.map { mapper.map(it) } }
    }
}