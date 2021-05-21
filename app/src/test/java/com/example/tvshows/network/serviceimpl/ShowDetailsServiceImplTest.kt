package com.example.tvshows.network.serviceimpl

import com.example.tvshows.FileReader
import com.example.tvshows.database.model.ShowDetails
import com.example.tvshows.network.api.ShowsApi
import com.example.tvshows.network.interceptor.ShowsAuthorizationInterceptor
import com.example.tvshows.network.mapper.Mapper
import com.example.tvshows.network.remoteModel.RemoteShowDetails
import com.example.tvshows.utilities.API_KEY
import com.google.gson.FieldNamingPolicy
import com.google.gson.GsonBuilder
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class ShowDetailsServiceImplTest {

    private val mapper: Mapper<RemoteShowDetails, ShowDetails> = mock()
    private lateinit var webServer: MockWebServer
    private lateinit var showDetailsServiceImpl: ShowDetailsServiceImpl

    private val showId = 1
    private val showDetails: ShowDetails = mock()

    @Before
    fun setUp() {
        whenever(mapper.map(any())).thenReturn(showDetails)

        webServer = MockWebServer()
        val serverURL = webServer.url("/").toString()

        val client = OkHttpClient.Builder()
            .addInterceptor(ShowsAuthorizationInterceptor())
            .build()

        val gson = GsonBuilder()
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .create()

        val showsApi = Retrofit.Builder()
            .baseUrl(serverURL)
            .client(client)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(ShowsApi::class.java)

        showDetailsServiceImpl = ShowDetailsServiceImpl(showsApi, mapper)
    }

    @After
    fun cleanUp() {
        webServer.shutdown()
    }

    @Test
    fun test_successful_response() {
        // arrange
        val json = FileReader.readFile("response_get_show_details_200.json")
        val mockResponse = MockResponse()
            .setResponseCode(200)
            .setBody(json)
        webServer.enqueue(mockResponse)

        // assert
        showDetailsServiceImpl.getShowDetails(showId)
            .test()
            .assertValue(showDetails)
            .assertComplete()
            .dispose()
    }

    @Test
    fun test_request() {
        // arrange
        val json = FileReader.readFile("response_get_show_details_200.json")
        val mockResponse = MockResponse()
            .setResponseCode(200)
            .setBody(json)
        webServer.enqueue(mockResponse)

        // assert
        showDetailsServiceImpl.getShowDetails(showId = showId)
            .test()
            .dispose()

        val request: RecordedRequest = webServer.takeRequest()
        assertEquals("/3/tv/$showId?api_key=$API_KEY", request.path)
    }
}