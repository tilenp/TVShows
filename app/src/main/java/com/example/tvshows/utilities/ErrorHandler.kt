package com.example.tvshows.utilities

import android.content.Context
import android.widget.Toast
import org.json.JSONObject
import retrofit2.HttpException
import javax.inject.Inject

class ErrorHandler @Inject constructor(
    private val context: Context
) {

    fun handleError(throwable: Throwable) {
        when (throwable) {
            is HttpException -> handleHttpException(throwable)
            else -> showToast(throwable.message)
        }
    }

    private fun handleHttpException(exception: HttpException) {
        exception.response()?.errorBody()?.string()?.let { errorString ->
            try {
                val json = JSONObject(errorString)
                showToast(json.getString("status_message"))
            } catch (e: Exception) {
            }
        }
    }

    private fun showToast(message: String?) {
        message?.let { Toast.makeText(context, message, Toast.LENGTH_LONG).show() }
    }
}