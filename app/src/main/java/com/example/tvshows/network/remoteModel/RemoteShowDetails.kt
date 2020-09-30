package com.example.tvshows.network.remoteModel

data class RemoteShowDetails(
    val id: Int?,
    val voteAverage: Float?,
    val voteCount: Int?,
    val overview: String?,
    val seasons: List<RemoteSeason>?
)