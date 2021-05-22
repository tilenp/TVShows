package com.example.tvshows.network.mapper

import android.content.Context
import com.example.tvshows.R
import com.example.tvshows.database.model.ImagePath
import com.example.tvshows.database.table.ShowSummary
import com.example.tvshows.network.remoteModel.RemoteShowSummary
import com.example.tvshows.utilities.mapString
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ShowSummaryMapper @Inject constructor(
    private val context: Context
): Mapper<RemoteShowSummary, ShowSummary> {

    override fun map(objectToMap: RemoteShowSummary): ShowSummary {
        return ShowSummary(
            showId = objectToMap.id ?: 0,
            name = mapString(objectToMap.name, context.getString(R.string.Title_not_available)),
            imagePath = objectToMap.posterPath?.let { ImagePath(it) }
        )
    }
}