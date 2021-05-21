package com.example.tvshows.network.serviceimpl

import com.example.tvshows.FileReader
import com.example.tvshows.network.api.ShowsApi
import com.example.tvshows.network.interceptor.ShowsAuthorizationInterceptor
import com.example.tvshows.network.mapper.Mapper
import com.example.tvshows.network.remoteModel.RemoteShowSummary
import com.example.tvshows.network.remoteModel.RemoteWrapper
import com.example.tvshows.repository.paging.ShowSummariesWrapper
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


class ShowSummariesWrapperServiceImplTest {

    private val mapper: Mapper<RemoteWrapper<List<RemoteShowSummary>>, ShowSummariesWrapper> = mock()
    private lateinit var webServer: MockWebServer
    private lateinit var showSummariesWrapperServiceImpl: ShowSummariesWrapperServiceImpl

    private val page = 1
    private val showSummariesWrapper: ShowSummariesWrapper = mock()

    @Before
    fun setUp() {
        whenever(mapper.map(any())).thenReturn(showSummariesWrapper)

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

        showSummariesWrapperServiceImpl = ShowSummariesWrapperServiceImpl(showsApi, mapper)
    }

    @After
    fun cleanUp() {
        webServer.shutdown()
    }

    @Test
    fun test_successful_response() {
        // arrange
        val json = FileReader.readFile("response_get_show_summaries_200.json")
        val mockResponse = MockResponse()
            .setResponseCode(200)
            .setBody(json)
        webServer.enqueue(mockResponse)

        // assert
        showSummariesWrapperServiceImpl.getShowSummariesWrapper(page)
            .test()
            .assertValue(showSummariesWrapper)
            .assertNoErrors()
            .assertComplete()
            .dispose()
    }

    @Test
    fun test_request() {
        // arrange
        val json = FileReader.readFile("response_get_show_summaries_200.json")
        val mockResponse = MockResponse()
            .setResponseCode(200)
            .setBody(json)
        webServer.enqueue(mockResponse)

        // assert
        showSummariesWrapperServiceImpl.getShowSummariesWrapper(page)
            .test()
            .dispose()

        val request: RecordedRequest = webServer.takeRequest()
        assertEquals("/3/discover/tv?sort_by=popularity.desc&page=$page&api_key=$API_KEY", request.path)
    }
}