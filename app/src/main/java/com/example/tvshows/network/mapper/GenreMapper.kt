package com.example.tvshows.network.mapper

import android.content.Context
import com.example.tvshows.R
import com.example.tvshows.database.table.Genre
import com.example.tvshows.network.remoteModel.RemoteGenre
import com.example.tvshows.utilities.mapString
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GenreMapper @Inject constructor(
    private val context: Context
) : Mapper<RemoteGenre, Genre> {

    override fun map(objectToMap: RemoteGenre): Genre {
        return Genre(
            name = mapString(objectToMap.name, context.getString(R.string.Genres_not_available))
        )
    }
}