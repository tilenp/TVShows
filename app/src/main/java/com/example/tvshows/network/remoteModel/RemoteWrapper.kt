package com.example.tvshows.network.remoteModel

data class RemoteWrapper<T>(
    val totalPages: Int = 0,
    val page: Int = 0,
    val results: T?
)