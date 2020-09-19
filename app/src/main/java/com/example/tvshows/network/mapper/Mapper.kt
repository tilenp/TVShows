package com.example.tvshows.network.mapper

interface Mapper<T, R> {
    fun map(objectToMap: T): R
}