package com.example.tvshows.database.dao

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.tvshows.database.TVShowsDatabase
import com.example.tvshows.database.model.PagingKeys
import kotlinx.coroutines.runBlocking
import org.hamcrest.Matchers.equalTo
import org.junit.After
import org.junit.Assert.assertNull
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class PagingKeysDaoTest {

    private lateinit var database: TVShowsDatabase
    private lateinit var pagingKeysDao: PagingKeysDao
    private val elementId1 = 1
    private val elementId2 = 2
    private val prevKey1 = 1
    private val prevKey2 = 2
    private val nextKey1 = 3
    private val nextKey2 = 4
    private val keys1 = PagingKeys(elementId = elementId1, prevKey = prevKey1, nextKey = nextKey1)
    private val keys2 = PagingKeys(elementId = elementId2, prevKey = prevKey2, nextKey = nextKey2)

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun createDb() = runBlocking {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        database = Room.inMemoryDatabaseBuilder(context, TVShowsDatabase::class.java).build()
        pagingKeysDao = database.getPagingKeysDao()
        pagingKeysDao.insertPagingKeys(listOf(keys1, keys2))
    }

    @After
    fun closeDb() {
        database.close()
    }

    @Test
    fun keys_are_inserted_successfully() {
        // assert
        val result1 = pagingKeysDao.getPagingKeysForElementId(elementId1)
        val result2 = pagingKeysDao.getPagingKeysForElementId(elementId2)
        assertThat(result1, equalTo(keys1))
        assertThat(result2, equalTo(keys2))
    }

    @Test
    fun keys_are_deleted_successfully() {
        // act
        pagingKeysDao.deletePagingKeys()

        // assert
        val result1 = pagingKeysDao.getPagingKeysForElementId(elementId1)
        val result2 = pagingKeysDao.getPagingKeysForElementId(elementId2)
        assertNull(result1)
        assertNull(result2)
    }
}