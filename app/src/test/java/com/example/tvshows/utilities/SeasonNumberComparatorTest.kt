package com.example.tvshows.utilities

import com.example.tvshows.database.table.Season
import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.*

class SeasonNumberComparatorTest {

    private val comparator = SeasonNumberComparator()

    @Test
    fun seasons_are_ordered_by_season_number() {
        // arrange
        val season1 = Season(seasonNumber = 1)
        val season2 = Season(seasonNumber = 2)
        val season3 = Season(seasonNumber = 3)
        val seasons = listOf(season2, season1, season3)

        // act
        Collections.sort(seasons, comparator)

        // assert
        assertEquals(season1, seasons[0])
        assertEquals(season2, seasons[1])
        assertEquals(season3, seasons[2])
    }

    @Test
    fun null_seasons_are_pushed_back() {
        // arrange
        val season1 = Season(seasonNumber = 1)
        val season2 = null
        val season3 = null
        val seasons = listOf(season2, season1, season3)

        // act
        Collections.sort(seasons, comparator)

        // assert
        assertEquals(season1, seasons[0])
    }
}