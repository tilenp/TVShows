package com.example.tvshows.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.tvshows.database.dao.PagingKeysDao
import com.example.tvshows.database.dao.SeasonsDao
import com.example.tvshows.database.dao.ShowSummaryDao
import com.example.tvshows.database.model.PagingKeys
import com.example.tvshows.database.model.Season
import com.example.tvshows.database.model.ShowSummary
import com.example.tvshows.utilities.DATABASE_NAME

@Database(
    entities = [ShowSummary::class, PagingKeys::class, Season::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converter::class)
abstract class TVShowsDatabase : RoomDatabase() {

    abstract fun getShowSummaryDao(): ShowSummaryDao
    abstract fun getPagingKeysDao(): PagingKeysDao
    abstract fun getSeasonsDao(): SeasonsDao

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