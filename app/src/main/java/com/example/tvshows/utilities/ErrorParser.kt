package com.example.tvshows.utilities

import android.content.Context
import com.example.tvshows.R
import org.json.JSONObject
import retrofit2.HttpException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ErrorParser @Inject constructor(
    private val context: Context
) {

    fun parseError(throwable: Throwable): String {
        return when (throwable) {
            is HttpException -> handleHttpException(throwable)
            else -> throwable.message ?: context.getString(R.string.Unknown_error)
        }
    }

    private fun handleHttpException(exception: HttpException): String {
        exception.response()?.errorBody()?.string()?.let { errorString ->
            try {
                val json = JSONObject(errorString)
                return json.getString("status_message")
            } catch (e: Exception) {
            }
        }
        return context.getString(R.string.Unknown_error)
    }
}