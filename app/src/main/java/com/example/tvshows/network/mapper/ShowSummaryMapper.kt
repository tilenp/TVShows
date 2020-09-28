package com.example.tvshows.network.mapper

import com.example.tvshows.database.model.ImagePath
import com.example.tvshows.network.remoteModel.RemoteShowSummary
import com.example.tvshows.database.model.ShowSummary
import javax.inject.Inject

class ShowSummaryMapper @Inject constructor() : Mapper<RemoteShowSummary, ShowSummary> {

    override fun map(objectToMap: RemoteShowSummary): ShowSummary {
        return ShowSummary(
            showId = objectToMap.id ?: 0,
            name = objectToMap.name ?: "",
            imagePath = objectToMap.posterPath?.let { ImagePath(it) },
            rating = objectToMap.voteAverage ?: 0f,
            summary = objectToMap.overview ?: ""
        )
    }
}