package com.example.tvshows.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.tvshows.database.dao.SeasonsDao
import com.example.tvshows.database.dao.ShowsDao
import com.example.tvshows.database.model.Season
import com.example.tvshows.database.model.Show
import com.example.tvshows.utilities.DATABASE_NAME

@Database(
    entities = [Show::class, Season::class],
    version = 1,
    exportSchema = false
)
abstract class TVShowsDatabase : RoomDatabase() {

    abstract fun getShowsDao(): ShowsDao
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