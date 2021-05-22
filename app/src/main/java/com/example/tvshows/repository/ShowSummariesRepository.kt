package com.example.tvshows.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.rxjava2.flowable
import com.example.tvshows.database.dao.ShowSummaryDao
import com.example.tvshows.database.table.ShowSummary
import com.example.tvshows.repository.paging.ShowSummariesRemoteMediator
import io.reactivex.Flowable
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class)
class ShowSummariesRepository @Inject constructor(
    private val showSummaryDao: ShowSummaryDao,
    private val showSummariesRemoteMediator: ShowSummariesRemoteMediator
) {
    @OptIn(ExperimentalCoroutinesApi::class)
    fun getShowSummaries(pagingConfig: PagingConfig): Flowable<PagingData<ShowSummary>> {
        return Pager(
            config = pagingConfig,
            remoteMediator = showSummariesRemoteMediator,
            pagingSourceFactory = { showSummaryDao.getShowSummaries() }
        ).flowable
    }
}