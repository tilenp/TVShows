package com.example.tvshows.dagger.module

import com.example.tvshows.database.model.ShowSummary
import com.example.tvshows.network.mapper.Mapper
import com.example.tvshows.network.mapper.ShowSummaryMapper
import com.example.tvshows.network.mapper.ShowSummariesWrapperMapper
import com.example.tvshows.network.remoteModel.RemoteWrapper
import com.example.tvshows.network.remoteModel.RemoteShowSummary
import com.example.tvshows.repository.paging.ShowSummariesWrapper
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class MapperModule {

    @Singleton
    @Provides
    fun providesShowSummaryMapper(): Mapper<RemoteShowSummary, ShowSummary> {
        return ShowSummaryMapper()
    }

    @Singleton
    @Provides
    fun providesShowSummariesWrapperMapper(showSummaryMapper: Mapper<RemoteShowSummary, ShowSummary>): Mapper<RemoteWrapper<List<RemoteShowSummary>>, ShowSummariesWrapper> {
        return ShowSummariesWrapperMapper(showSummaryMapper)
    }
}