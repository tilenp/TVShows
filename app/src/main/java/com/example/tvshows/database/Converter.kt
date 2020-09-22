package com.example.tvshows.database

import androidx.room.TypeConverter
import com.example.tvshows.database.model.ImagePath

class Converter {

    @TypeConverter
    fun fromImagePath(image: ImagePath?): String? {
        return image?.url
    }

    @TypeConverter
    fun toImagePath(url: String?): ImagePath? {
        return url?.let { ImagePath(it) }
    }
}