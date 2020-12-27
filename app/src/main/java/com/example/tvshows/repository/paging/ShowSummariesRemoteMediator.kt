package com.example.tvshows.repository.paging

import androidx.annotation.VisibleForTesting
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.rxjava2.RxRemoteMediator
import com.example.tvshows.database.TVShowsDatabase
import com.example.tvshows.database.table.PagingKeys
import com.example.tvshows.database.table.ShowSummary
import com.example.tvshows.repository.service.ShowSummariesWrapperService
import com.example.tvshows.utilities.SchedulerProvider
import io.reactivex.Single
import java.io.InvalidObjectException
import javax.inject.Inject


@ExperimentalPagingApi
class ShowSummariesRemoteMediator @Inject constructor(
    private val showSummariesWrapperService: ShowSummariesWrapperService,
    private val database: TVShowsDatabase,
    private val schedulerProvider: SchedulerProvider
) : RxRemoteMediator<Int, ShowSummary>() {

    override fun loadSingle(
        loadType: LoadType,
        state: PagingState<Int, ShowSummary>
    ): Single<MediatorResult> {
        return Single.just(loadType)
            .map { calculatePage(it, state) }
            .flatMap { page ->
                if (page == INVALID_PAGE) {
                    Single.just(MediatorResult.Success(endOfPaginationReached = true))
                } else {
                    showSummariesWrapperService.getShowSummariesWrapper(page = page)
                        .map { wrapper -> updateDatabase(page, loadType, wrapper) }
                        .map { wrapper -> MediatorResult.Success(endOfPaginationReached = wrapper.endOfPaginationReached) as MediatorResult }
                        .onErrorReturn { MediatorResult.Error(it) }
                }
            }
            .onErrorResumeNext { error -> Single.error(error) }
            .subscribeOn(schedulerProvider.io())
    }

    @VisibleForTesting
    fun calculatePage(loadType: LoadType, state: PagingState<Int, ShowSummary>): Int {
        return when (loadType) {
            LoadType.REFRESH -> {
                val remoteKeys = getPagingKeysClosestToCurrentPosition(state)
                remoteKeys?.nextKey?.minus(1) ?: STARTING_PAGE
            }
            LoadType.PREPEND -> {
                val remoteKeys = getPagingKeysForFirstItem(state)
                    ?: throw InvalidObjectException("Result is empty")

                remoteKeys.prevKey ?: INVALID_PAGE
            }
            LoadType.APPEND -> {
                val remoteKeys = getPagingKeysForLastItem(state)
                    ?: throw InvalidObjectException("Result is empty")

                remoteKeys.nextKey ?: INVALID_PAGE
            }
        }
    }

    @VisibleForTesting
    fun getPagingKeysClosestToCurrentPosition(state: PagingState<Int, ShowSummary>): PagingKeys? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.showId?.let { id ->
                database.getPagingKeysDao().getPagingKeysForElementId(id)
            }
        }
    }

    @VisibleForTesting
    fun getPagingKeysForFirstItem(state: PagingState<Int, ShowSummary>): PagingKeys? {
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()?.let { show ->
            database.getPagingKeysDao().getPagingKeysForElementId(show.showId)
        }
    }

    @VisibleForTesting
    fun getPagingKeysForLastItem(state: PagingState<Int, ShowSummary>): PagingKeys? {
        return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()?.let { show ->
            database.getPagingKeysDao().getPagingKeysForElementId(show.showId)
        }
    }

    @VisibleForTesting
    fun updateDatabase(page: Int, loadType: LoadType, wrapper: ShowSummariesWrapper): ShowSummariesWrapper {
        database.runInTransaction {
            if (loadType == LoadType.REFRESH) {
                database.getPagingKeysDao().deletePagingKeys()
                database.getShowSummaryDao().deleteSummaries()
            }

            val prevKey = if (page == STARTING_PAGE) null else page - 1
            val nextKey = if (wrapper.endOfPaginationReached) null else page + 1
            val keys = wrapper.showSummaries.map {
                PagingKeys(elementId = it.showId, prevKey = prevKey, nextKey = nextKey)
            }
            database.getPagingKeysDao().insertPagingKeys(keys)
            database.getShowSummaryDao().insertShowSummaries(wrapper.showSummaries)
        }
        return wrapper
    }

    companion object {
        @VisibleForTesting const val STARTING_PAGE = 1
        @VisibleForTesting const val INVALID_PAGE = -1
    }
}