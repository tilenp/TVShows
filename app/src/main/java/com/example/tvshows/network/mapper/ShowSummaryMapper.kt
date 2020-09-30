package com.example.tvshows.network.mapper

import com.example.tvshows.database.model.ImagePath
import com.example.tvshows.database.model.ShowSummary
import com.example.tvshows.network.remoteModel.RemoteShowSummary

class ShowSummaryMapper: Mapper<RemoteShowSummary, ShowSummary> {

    override fun map(objectToMap: RemoteShowSummary): ShowSummary {
        return ShowSummary(
            showId = objectToMap.id ?: 0,
            name = objectToMap.name ?: "",
            imagePath = objectToMap.posterPath?.let { ImagePath(it) }
        )
    }
}