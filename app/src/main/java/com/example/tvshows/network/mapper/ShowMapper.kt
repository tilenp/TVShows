package com.example.tvshows.network.mapper

import com.example.tvshows.network.remoteModel.RemoteShow
import com.example.tvshows.database.Show
import javax.inject.Inject

class ShowMapper @Inject constructor(): Mapper<RemoteShow, Show> {

    override fun map(objectToMap: RemoteShow): Show {
        return Show(
            id = objectToMap.id ?: 0,
            image = objectToMap.image?.medium ?: "",
            rating = objectToMap.rating?.average ?: 0f,
            summary = objectToMap.summary ?: ""
        )
    }
}