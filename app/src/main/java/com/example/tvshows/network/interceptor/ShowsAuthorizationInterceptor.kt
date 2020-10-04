package com.example.tvshows.network.interceptor

import com.example.tvshows.utilities.API_KEY
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response


class ShowsAuthorizationInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val originalHttpUrl = originalRequest.url

        val newHttpUrl = originalHttpUrl.newBuilder()
            .setQueryParameter(API_KEY_PARAM, API_KEY)
            .build()
        val newRequest: Request = originalRequest.newBuilder()
            .url(newHttpUrl)
            .build()

        return chain.proceed(newRequest)
    }

    companion object {
        private const val API_KEY_PARAM = "api_key"
    }
}