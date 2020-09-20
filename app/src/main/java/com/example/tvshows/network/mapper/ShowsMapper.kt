package com.example.tvshows.network.mapper

import com.example.tvshows.database.model.Show
import com.example.tvshows.network.remoteModel.RemoteWrapper
import com.example.tvshows.network.remoteModel.RemoteShow
import com.example.tvshows.repository.paging.ShowsWrapper
import javax.inject.Inject

class ShowsMapper @Inject constructor(
    private val showMapper: Mapper<RemoteShow, Show>
) : Mapper<RemoteWrapper<List<RemoteShow>>, ShowsWrapper> {

    override fun map(objectToMap: RemoteWrapper<List<RemoteShow>>): ShowsWrapper {
        return ShowsWrapper(
            endOfPaginationReached = objectToMap.page == objectToMap.totalPages,
            shows = objectToMap.results?.map { showMapper.map(it) } ?: emptyList()
        )
    }
}