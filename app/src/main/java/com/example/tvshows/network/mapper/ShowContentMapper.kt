package com.example.tvshows.network.mapper

import android.content.Context
import com.example.tvshows.R
import com.example.tvshows.database.table.ShowContent
import com.example.tvshows.network.remoteModel.RemoteShowDetails
import com.example.tvshows.utilities.mapString
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ShowContentMapper @Inject constructor(
    private val context: Context
): Mapper<RemoteShowDetails, ShowContent> {

    override fun map(objectToMap: RemoteShowDetails): ShowContent {
        return ShowContent(
            showId = objectToMap.id ?: 0,
            summary = mapString(objectToMap.overview, context.getString(R.string.Summary_not_available))
        )
    }
}