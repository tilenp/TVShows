package com.example.tvshows.utilities

import com.example.tvshows.R
import org.json.JSONObject
import retrofit2.HttpException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ErrorParser @Inject constructor() {

    fun parseError(throwable: Throwable): Any {
        return when (throwable) {
            is HttpException -> handleHttpException(throwable)
            else -> throwable.message ?: R.string.unknown_error
        }
    }

    private fun handleHttpException(exception: HttpException): Any {
        exception.response()?.errorBody()?.string()?.let { errorString ->
            try {
                val json = JSONObject(errorString)
                return json.getString("status_message")
            } catch (e: Exception) {
            }
        }
        return R.string.unknown_error
    }
}