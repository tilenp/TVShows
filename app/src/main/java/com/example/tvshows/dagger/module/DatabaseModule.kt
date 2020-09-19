package com.example.tvshows.dagger.module

import android.content.Context
import com.example.tvshows.database.dao.SeasonsDao
import com.example.tvshows.database.dao.ShowsDao
import com.example.tvshows.database.TVShowsDatabase
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
    fun providesShowsDao(database: TVShowsDatabase): ShowsDao {
        return database.getShowsDao()
    }

    @Singleton
    @Provides
    fun providesSeasonsDao(database: TVShowsDatabase): SeasonsDao {
        return database.getSeasonsDao()
    }
}