package com.example.tvshows.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.tvshows.database.dao.*
import com.example.tvshows.database.model.*
import com.example.tvshows.utilities.DATABASE_NAME

@Database(
    entities = [ShowSummary::class, PagingKeys::class, ShowContent::class, Genre::class, Season::class],
    version = 1,
    exportSchema = true
)
@TypeConverters(Converter::class)
abstract class TVShowsDatabase : RoomDatabase() {

    abstract fun getShowSummaryDao(): ShowSummaryDao
    abstract fun getPagingKeysDao(): PagingKeysDao
    abstract fun getShowDetailsDao(): ShowDetailsDao
    abstract fun getSeasonsDao(): SeasonsDao
    abstract fun getGenreDao(): GenreDao

    companion object {

        @Volatile
        private var INSTANCE: TVShowsDatabase? = null

        fun getInstance(context: Context): TVShowsDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE
                    ?: buildDatabase(context).also { INSTANCE = it }
            }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(context.applicationContext,
                TVShowsDatabase::class.java, DATABASE_NAME)
                .build()
    }
}