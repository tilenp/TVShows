package com.example.tvshows.database.model

import android.net.Uri
import com.example.tvshows.utilities.*

data class ImagePath(val url: String) {

    val small: Uri = Uri.parse("$IMAGE_URL$SMALL$url")

    val medium: Uri = Uri.parse("$IMAGE_URL$MEDIUM$url")

    val large: Uri = Uri.parse("$IMAGE_URL$LARGE$url")

    val original: Uri = Uri.parse("$IMAGE_URL$ORIGINAL$url")
}