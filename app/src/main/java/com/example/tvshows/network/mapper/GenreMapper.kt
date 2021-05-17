package com.example.tvshows.network.mapper

import com.example.tvshows.database.table.Genre
import com.example.tvshows.network.remoteModel.RemoteGenre
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GenreMapper @Inject constructor(): Mapper<RemoteGenre, Genre> {

    override fun map(objectToMap: RemoteGenre): Genre {
        return Genre(
            name = objectToMap.name ?: ""
        )
    }
}