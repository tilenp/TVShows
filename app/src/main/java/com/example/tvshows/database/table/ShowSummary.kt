package com.example.tvshows.database.table

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.tvshows.database.model.ImagePath

@Entity(indices = [Index(value = ["showId"], unique = true)])
data class ShowSummary(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    var showId: Int = 0,
    var name: String = "",
    var imagePath: ImagePath? = null
)