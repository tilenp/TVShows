package com.example.tvshows.dagger.module

import android.content.Context
import com.example.tvshows.database.TVShowsDatabase
import com.example.tvshows.database.dao.*
import dagger.Module
import dagger.Provides

import javax.inject.Singleton


@Module
class DatabaseModule {

    @Singleton
    @Provides
    fun providesAppDatabase(context: Context): TVShowsDatabase {
        return TVShowsDatabase.getInstance(context)
    }

    @Singleton
    @Provides
    fun providesShowSummaryDao(database: TVShowsDatabase): ShowSummaryDao {
        return database.getShowSummaryDao()
    }

    @Singleton
    @Provides
    fun providesPagingKeysDao(database: TVShowsDatabase): PagingKeysDao {
        return database.getPagingKeysDao()
    }

    @Singleton
    @Provides
    fun providesShowDetailsDao(database: TVShowsDatabase): ShowDetailsDao {
        return database.getShowDetailsDao()
    }
}