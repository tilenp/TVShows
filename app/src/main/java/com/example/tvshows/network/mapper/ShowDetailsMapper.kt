package com.example.tvshows.network.mapper

import com.example.tvshows.database.model.Genre
import com.example.tvshows.database.model.Season
import com.example.tvshows.database.model.ShowContent
import com.example.tvshows.database.model.ShowDetails
import com.example.tvshows.network.remoteModel.RemoteGenre
import com.example.tvshows.network.remoteModel.RemoteSeason
import com.example.tvshows.network.remoteModel.RemoteShowDetails

class ShowDetailsMapper constructor(
    private val showContentMapper: Mapper<RemoteShowDetails, ShowContent>,
    private val genreMapper: Mapper<RemoteGenre, Genre>,
    private val seasonMapper: Mapper<RemoteSeason, Season>
) : Mapper<RemoteShowDetails, ShowDetails> {

    override fun map(objectToMap: RemoteShowDetails): ShowDetails {
        return ShowDetails(
            showContent = showContentMapper.map(objectToMap),
            genres = objectToMap.genres?.map { mapGenre(it, objectToMap.id) } ?: emptyList(),
            seasons = objectToMap.seasons?.map { mapSeason(it, objectToMap.id) } ?: emptyList()
        )
    }

    private fun mapGenre(remoteGenre: RemoteGenre, showId: Int?): Genre {
        return genreMapper.map(remoteGenre).also { it.showId = showId ?: 0 }
    }

    private fun mapSeason(remoteSeason: RemoteSeason, showId: Int?): Season {
        return seasonMapper.map(remoteSeason).also { it.showId = showId ?: 0 }
    }
}