package com.example.tvshows.dagger.module

import com.example.tvshows.database.model.Genre
import com.example.tvshows.database.model.Season
import com.example.tvshows.database.model.ShowDetails
import com.example.tvshows.database.model.ShowSummary
import com.example.tvshows.network.mapper.*
import com.example.tvshows.network.remoteModel.*
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
    fun providesGenreMapper(): Mapper<RemoteGenre, Genre> {
        return GenreMapper()
    }

    @Singleton
    @Provides
    fun providesSeasonMapper(): Mapper<RemoteSeason, Season> {
        return SeasonMapper()
    }

    @Singleton
    @Provides
    fun providesShowDetailsMapper(
        genreMapper: Mapper<RemoteGenre, Genre>,
        seasonMapper: Mapper<RemoteSeason, Season>
    ): Mapper<RemoteShowDetails, ShowDetails> {
        return ShowDetailsMapper(genreMapper, seasonMapper)
    }
}