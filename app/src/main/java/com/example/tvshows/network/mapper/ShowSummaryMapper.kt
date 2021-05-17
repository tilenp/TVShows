package com.example.tvshows.network.mapper

import com.example.tvshows.database.model.ImagePath
import com.example.tvshows.database.table.ShowSummary
import com.example.tvshows.network.remoteModel.RemoteShowSummary
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ShowSummaryMapper @Inject constructor(): Mapper<RemoteShowSummary, ShowSummary> {

    override fun map(objectToMap: RemoteShowSummary): ShowSummary {
        return ShowSummary(
            showId = objectToMap.id ?: 0,
            name = objectToMap.name ?: "",
            imagePath = objectToMap.posterPath?.let { ImagePath(it) }
        )
    }
}