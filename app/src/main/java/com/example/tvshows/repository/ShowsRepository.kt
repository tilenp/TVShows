package com.example.tvshows.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.rxjava2.flowable
import com.example.tvshows.database.dao.ShowsDao
import com.example.tvshows.database.model.Show
import com.example.tvshows.repository.paging.ShowsRemoteMediator
import io.reactivex.Flowable
import javax.inject.Inject

class ShowsRepository @Inject constructor(
    private val showsDao: ShowsDao,
    private val showsRemoteMediator: ShowsRemoteMediator
){
    fun getShows(): Flowable<PagingData<Show>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = true,
                prefetchDistance = 5,
                initialLoadSize = 40),
            remoteMediator = showsRemoteMediator,
            pagingSourceFactory = { showsDao.getShows() }
        ).flowable
    }
}