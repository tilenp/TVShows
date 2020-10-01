package com.example.tvshows.network.mapper

import com.example.tvshows.database.table.ShowContent
import com.example.tvshows.network.remoteModel.RemoteShowDetails

class ShowContentMapper: Mapper<RemoteShowDetails, ShowContent> {

    override fun map(objectToMap: RemoteShowDetails): ShowContent {
        return ShowContent(
            showId = objectToMap.id ?: 0,
            summary = objectToMap.overview ?: ""
        )
    }
}