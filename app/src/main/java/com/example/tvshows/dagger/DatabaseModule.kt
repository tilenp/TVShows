package com.example.tvshows.dagger

import android.content.Context
import com.example.tvshows.database.DaoSeasons
import com.example.tvshows.database.DaoShows
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
    fun providesDaoShows(database: TVShowsDatabase): DaoShows {
        return database.getDaoShows()
    }

    @Singleton
    @Provides
    fun providesDaoSeasons(database: TVShowsDatabase): DaoSeasons {
        return database.getDaoSeasons()
    }
}