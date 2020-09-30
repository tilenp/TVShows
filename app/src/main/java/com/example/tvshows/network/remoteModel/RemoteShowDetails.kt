package com.example.tvshows.network.remoteModel

data class RemoteShowDetails(
    val id: Int?,
    val genres: List<RemoteGenre>?,
    val overview: String?,
    val seasons: List<RemoteSeason>?
)