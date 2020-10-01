package com.example.tvshows.network.mapper

import com.example.tvshows.database.table.Genre
import com.example.tvshows.network.remoteModel.RemoteGenre

class GenreMapper : Mapper<RemoteGenre, Genre> {

    override fun map(objectToMap: RemoteGenre): Genre {
        return Genre(
            name = objectToMap.name ?: ""
        )
    }
}