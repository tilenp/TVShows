package com.example.tvshows.repository.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.rxjava2.RxRemoteMediator
import com.example.tvshows.database.TVShowsDatabase
import com.example.tvshows.database.model.RemoteKeys
import com.example.tvshows.database.model.Show
import com.example.tvshows.repository.service.ShowsService
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import java.io.InvalidObjectException
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class)
class ShowsRemoteMediator @Inject constructor(
    private val showsService: ShowsService,
    private val database: TVShowsDatabase
) : RxRemoteMediator<Int, Show>() {

    override fun loadSingle(
        loadType: LoadType,
        state: PagingState<Int, Show>
    ): Single<MediatorResult> {
        return Single.just(loadType)
            .map { calculatePage(it, state) }
            .flatMap { page ->
                if (page == INVALID_PAGE) {
                    Single.just(MediatorResult.Success(endOfPaginationReached = true))
                } else {
                    showsService.getShows(page = page)
                        .map { shows -> updateDatabase(page, loadType, shows) }
                        .map { shows -> MediatorResult.Success(endOfPaginationReached = shows.endOfPaginationReached) as MediatorResult }
                        .onErrorReturn { MediatorResult.Error(it) }
                }
            }
            .onErrorReturn { MediatorResult.Error(it) }
            .subscribeOn(Schedulers.io())
    }

    private fun calculatePage(loadType: LoadType, state: PagingState<Int, Show>): Int {
        return when (loadType) {
            LoadType.REFRESH -> {
                val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                remoteKeys?.nextKey?.minus(1) ?: STARTING_PAGE
            }
            LoadType.PREPEND -> {
                val remoteKeys = getRemoteKeyForFirstItem(state)
                    ?: throw InvalidObjectException("Result is empty")

                remoteKeys.prevKey ?: INVALID_PAGE
            }
            LoadType.APPEND -> {
                val remoteKeys = getRemoteKeyForLastItem(state)
                    ?: throw InvalidObjectException("Result is empty")

                remoteKeys.nextKey ?: INVALID_PAGE
            }
        }
    }

    private fun getRemoteKeyClosestToCurrentPosition(state: PagingState<Int, Show>): RemoteKeys? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.showId?.let { id ->
                database.getShowsDao().getRemoteKeysForShowId(id)
            }
        }
    }

    private fun getRemoteKeyForFirstItem(state: PagingState<Int, Show>): RemoteKeys? {
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()?.let { show ->
            database.getShowsDao().getRemoteKeysForShowId(show.showId)
        }
    }

    private fun getRemoteKeyForLastItem(state: PagingState<Int, Show>): RemoteKeys? {
        return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()?.let { show ->
            database.getShowsDao().getRemoteKeysForShowId(show.showId)
        }
    }

    private fun updateDatabase(page: Int, loadType: LoadType, wrapper: ShowsWrapper): ShowsWrapper {
        database.runInTransaction {
            if (loadType == LoadType.REFRESH) {
                database.getShowsDao().deleteRemoteKeys()
                database.getShowsDao().deleteShows()
            }

            val prevKey = if (page == STARTING_PAGE) null else page - 1
            val nextKey = if (wrapper.endOfPaginationReached) null else page + 1
            val keys = wrapper.shows.map {
                RemoteKeys(showId = it.showId, prevKey = prevKey, nextKey = nextKey)
            }
            database.getShowsDao().insertRemoteKeys(keys)
            database.getShowsDao().insertShows(wrapper.shows)
        }
        return wrapper
    }

    companion object {
        private const val STARTING_PAGE = 1
        private const val INVALID_PAGE = -1
    }
}