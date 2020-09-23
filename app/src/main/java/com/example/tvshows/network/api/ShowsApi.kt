package com.example.tvshows.network.api

import com.example.tvshows.network.remoteModel.RemoteShow
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
import retrofit2.http.Query

interface ShowsApi {

    @GET("3/discover/tv?sort_by=popularity.desc")
    fun getShows(
        @Query("api_key") apiKey: String = API_KEY,
        @Query("page") page: Int
    ): Single<RemoteWrapper<List<RemoteShow>>>

    companion object {
        fun create(
            logger: HttpLoggingInterceptor,
            client: OkHttpClient,
            callAdapterFactory: RxJava2CallAdapterFactory,
            converterFactory: GsonConverterFactory
        ): ShowsApi {
            logger.level = HttpLoggingInterceptor.Level.BODY

            client.newBuilder()
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