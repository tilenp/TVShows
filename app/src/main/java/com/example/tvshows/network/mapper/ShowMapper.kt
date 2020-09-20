package com.example.tvshows.network.mapper

import com.example.tvshows.database.model.ImagePath
import com.example.tvshows.network.remoteModel.RemoteShow
import com.example.tvshows.database.model.Show
import javax.inject.Inject

class ShowMapper @Inject constructor() : Mapper<RemoteShow, Show> {

    override fun map(objectToMap: RemoteShow): Show {
        return Show(
            showId = objectToMap.id ?: 0,
            imagePath = objectToMap.posterPath?.let { ImagePath(it) },
            rating = objectToMap.voteAverage ?: 0f,
            summary = objectToMap.overview ?: ""
        )
    }
}