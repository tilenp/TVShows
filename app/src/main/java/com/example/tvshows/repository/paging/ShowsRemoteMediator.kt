package com.example.tvshows.repository.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.example.tvshows.database.model.Show
import com.example.tvshows.database.TVShowsDatabase
import com.example.tvshows.network.api.ShowsApi
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class)
class ShowsRemoteMediator @Inject constructor(
    private val showsApi: ShowsApi,
    private val database: TVShowsDatabase
): RemoteMediator<Int, Show>() {

    override suspend fun load(loadType: LoadType, state: PagingState<Int, Show>): MediatorResult {
        TODO("Not yet implemented")
    }
}