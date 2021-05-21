package com.example.tvshows.database.table

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    foreignKeys = [ForeignKey(
        entity = ShowSummary::class,
        parentColumns = ["showId"],
        childColumns = ["showId"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index(value = ["showId"])]
)
data class Genre (
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    var showId: Int = 0,
    val name: String = ""
) {
    override fun toString(): String {
        return name
    }
}