package com.example.tvshows.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.rxjava2.flowable
import com.example.tvshows.database.dao.ShowSummaryDao
import com.example.tvshows.database.table.ShowSummary
import com.example.tvshows.repository.paging.ShowSummariesRemoteMediator
import com.example.tvshows.utilities.INITIAL_LOAD_SIZE
import com.example.tvshows.utilities.MAX_SIZE
import com.example.tvshows.utilities.PAGE_SIZE
import com.example.tvshows.utilities.PREFETCH_DISTANCE
import io.reactivex.Flowable
import javax.inject.Inject
import javax.inject.Named

@ExperimentalPagingApi
class ShowSummariesRepository @Inject constructor(
    private val showSummaryDao: ShowSummaryDao,
    private val showSummariesRemoteMediator: ShowSummariesRemoteMediator
){
    fun getShowSummaries(pagingConfig: PagingConfig): Flowable<PagingData<ShowSummary>> {
        return Pager(
            config = pagingConfig,
            remoteMediator = showSummariesRemoteMediator,
            pagingSourceFactory = { showSummaryDao.getShowSummaries() }
        ).flowable
    }
}