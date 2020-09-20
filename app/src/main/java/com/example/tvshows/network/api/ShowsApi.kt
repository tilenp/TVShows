package com.example.tvshows.network.api

import com.example.tvshows.network.remoteModel.RemoteWrapper
import com.example.tvshows.network.remoteModel.RemoteShow
import com.example.tvshows.utilities.API_KEY
import com.example.tvshows.utilities.BASE_URL
import com.google.gson.FieldNamingPolicy
import com.google.gson.GsonBuilder
import io.reactivex.Single
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface ShowsApi {

    @GET("3/discover/tv?sort_by=popularity.desc")
    fun getShows(
        @Query("api_key") apiKey: String = API_KEY,
        @Query("page") page: Int
    ): Single<RemoteWrapper<List<RemoteShow>>>

    companion object {
        fun create(): ShowsApi {
            val logger = HttpLoggingInterceptor()
            logger.level = HttpLoggingInterceptor.Level.BASIC

            val client = OkHttpClient.Builder()
                .addInterceptor(logger)
                .build()

            val gson = GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create()

            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
                .create(ShowsApi::class.java)
        }
    }
}