package com.example.tvshows.database.model

import com.example.tvshows.utilities.*

data class ImagePath(val url: String) {

    val small = "$IMAGE_URL$SMALL$url"

    val medium = "$IMAGE_URL$MEDIUM$url"

    val large = "$IMAGE_URL$LARGE$url"

    val original = "$IMAGE_URL$ORIGINAL$url"
}