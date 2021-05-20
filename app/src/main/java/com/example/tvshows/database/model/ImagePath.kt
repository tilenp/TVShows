package com.example.tvshows.database.model

import com.example.tvshows.utilities.*

data class ImagePath(val url: String) {
    val medium = "$IMAGE_URL$MEDIUM$url"
}