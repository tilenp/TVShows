package com.example.tvshows.network.mapper

import com.example.tvshows.database.model.Season
import com.example.tvshows.database.model.ShowDetails
import com.example.tvshows.network.remoteModel.RemoteSeason
import com.example.tvshows.network.remoteModel.RemoteShowDetails

class ShowDetailsMapper constructor(
    private val seasonMapper: Mapper<RemoteSeason, Season>
) : Mapper<RemoteShowDetails, ShowDetails> {

    override fun map(objectToMap: RemoteShowDetails): ShowDetails {
        return ShowDetails(
            showId = objectToMap.id ?: 0,
            rating = objectToMap.voteAverage ?: 0f,
            ratingCount = objectToMap.voteCount ?: 0,
            summary = objectToMap.overview ?: "",
            seasons = objectToMap.seasons?.map { mapSeason(it, objectToMap.id) } ?: emptyList()
        )
    }

    private fun mapSeason(remoteSeason: RemoteSeason, showId: Int?): Season {
        return seasonMapper.map(remoteSeason).also { it.showId = showId ?: 0 }
    }
}