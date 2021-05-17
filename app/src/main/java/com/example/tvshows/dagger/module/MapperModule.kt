package com.example.tvshows.dagger.module

import com.example.tvshows.database.model.ShowDetails
import com.example.tvshows.database.table.Genre
import com.example.tvshows.database.table.Season
import com.example.tvshows.database.table.ShowContent
import com.example.tvshows.database.table.ShowSummary
import com.example.tvshows.network.mapper.*
import com.example.tvshows.network.remoteModel.*
import com.example.tvshows.repository.paging.ShowSummariesWrapper
import dagger.Binds
import dagger.Module

@Module
interface MapperModule {

    @Binds
    fun bindsShowSummaryMapper(showSummaryMapper: ShowSummaryMapper): Mapper<RemoteShowSummary, ShowSummary>

    @Binds
    fun bindsShowSummariesWrapperMapper(showSummariesWrapperMapper: ShowSummariesWrapperMapper): Mapper<RemoteWrapper<List<@JvmSuppressWildcards RemoteShowSummary>>, ShowSummariesWrapper>

    @Binds
    fun bindsShowContentMapper(showContentMapper: ShowContentMapper): Mapper<RemoteShowDetails, ShowContent>

    @Binds
    fun bindsGenreMapper(genreMapper: GenreMapper): Mapper<RemoteGenre, Genre>

    @Binds
    fun bindsSeasonMapper(seasonMapper: SeasonMapper): Mapper<RemoteSeason, Season>

    @Binds
    fun bindsShowDetailsMapper(showDetailsMapper: ShowDetailsMapper): Mapper<RemoteShowDetails, ShowDetails>
}