package com.example.tvshows.network.remoteModel

data class RemoteShow(
    val id: Int?,
    val name: String?,
    val rating: RemoteRating?,
    val image: RemoteImage?,
    val summary: String?
)
