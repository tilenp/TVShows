package com.example.tvshows.network.mapper

import android.content.Context
import com.example.tvshows.R
import com.example.tvshows.database.model.ImagePath
import com.example.tvshows.database.table.Season
import com.example.tvshows.network.remoteModel.RemoteSeason
import com.example.tvshows.utilities.mapString
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SeasonMapper @Inject constructor(
    private val context: Context
) : Mapper<RemoteSeason, Season> {

    override fun map(objectToMap: RemoteSeason): Season {
        return Season(
            id = objectToMap.id ?: 0,
            name = mapString(objectToMap.name, context.getString(R.string.Season_name_not_available)),
            seasonNumber = objectToMap.seasonNumber ?: 0,
            imagePath = objectToMap.posterPath?.let { ImagePath(it) }
        )
    }
}