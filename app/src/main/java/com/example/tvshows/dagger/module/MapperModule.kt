package com.example.tvshows.dagger.module

import com.example.tvshows.database.model.Season
import com.example.tvshows.database.model.ShowDetails
import com.example.tvshows.database.model.ShowSummary
import com.example.tvshows.network.mapper.*
import com.example.tvshows.network.remoteModel.RemoteSeason
import com.example.tvshows.network.remoteModel.RemoteShowDetails
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

    @Singleton
    @Provides
    fun providesSeasonMapper(): Mapper<RemoteSeason, Season> {
        return SeasonMapper()
    }

    @Singleton
    @Provides
    fun providesShowDetailsMapper(seasonMapper: Mapper<RemoteSeason, Season>): Mapper<RemoteShowDetails, ShowDetails> {
        return ShowDetailsMapper(seasonMapper)
    }
}