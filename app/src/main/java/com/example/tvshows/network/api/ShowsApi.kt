package com.example.tvshows.network.api

import com.example.tvshows.network.remoteModel.RemoteShowDetails
import com.example.tvshows.network.remoteModel.RemoteShowSummary
import com.example.tvshows.network.remoteModel.RemoteWrapper
import com.example.tvshows.utilities.API_KEY
import com.example.tvshows.utilities.BASE_URL
import io.reactivex.Single
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ShowsApi {

    @GET("3/discover/tv?sort_by=popularity.desc")
    fun getShowSummaries(
        @Query("api_key") apiKey: String = API_KEY,
        @Query("page") page: Int
    ): Single<RemoteWrapper<List<RemoteShowSummary>>>

    @GET("3/tv/{tv_id}")
    fun getShowDetails(
        @Path("tv_id") showId: Int,
        @Query("api_key") apiKey: String = API_KEY
    ): Single<RemoteShowDetails>

    companion object {
        fun create(
            logger: HttpLoggingInterceptor,
            callAdapterFactory: RxJava2CallAdapterFactory,
            converterFactory: GsonConverterFactory
        ): ShowsApi {
            logger.level = HttpLoggingInterceptor.Level.BODY

            val client = OkHttpClient.Builder()
                .addInterceptor(logger)
                .build()

            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addCallAdapterFactory(callAdapterFactory)
                .addConverterFactory(converterFactory)
                .build()
                .create(ShowsApi::class.java)
        }
    }
}