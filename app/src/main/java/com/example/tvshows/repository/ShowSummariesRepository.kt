package com.example.tvshows.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.rxjava2.flowable
import com.example.tvshows.database.dao.ShowSummaryDao
import com.example.tvshows.database.model.ShowSummary
import com.example.tvshows.repository.paging.ShowSummariesRemoteMediator
import io.reactivex.Flowable
import javax.inject.Inject

class ShowSummariesRepository @Inject constructor(
    private val showSummaryDao: ShowSummaryDao,
    private val showSummariesRemoteMediator: ShowSummariesRemoteMediator
){
    fun getShowSummaries(): Flowable<PagingData<ShowSummary>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = true,
                maxSize = 30,
                prefetchDistance = 5,
                initialLoadSize = 40),
            remoteMediator = showSummariesRemoteMediator,
            pagingSourceFactory = { showSummaryDao.getShowSummaries() }
        ).flowable
    }
}