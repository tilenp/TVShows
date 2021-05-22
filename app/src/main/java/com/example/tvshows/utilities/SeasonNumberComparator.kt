package com.example.tvshows.utilities

import com.example.tvshows.database.table.Season

class SeasonNumberComparator : Comparator<Season?> {

    override fun compare(season1: Season?, season2: Season?): Int {
        return when {
            season1 == null && season2 == null -> 0
            season1 == null -> 1
            season2 == null -> -1
            else -> season1.seasonNumber.compareTo(season2.seasonNumber)
        }
    }
}