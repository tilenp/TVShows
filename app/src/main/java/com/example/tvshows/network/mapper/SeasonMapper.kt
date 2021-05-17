package com.example.tvshows.network.mapper

import com.example.tvshows.database.model.ImagePath
import com.example.tvshows.database.table.Season
import com.example.tvshows.network.remoteModel.RemoteSeason
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SeasonMapper @Inject constructor(): Mapper<RemoteSeason, Season> {

    override fun map(objectToMap: RemoteSeason): Season {
        return Season(
            id = objectToMap.id ?: 0,
            name = objectToMap.name ?: "",
            seasonNumber = objectToMap.seasonNumber ?: 0,
            imagePath = objectToMap.posterPath?.let { ImagePath(it) }
        )
    }
}