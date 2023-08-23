package com.kamancho.melisma.popular.domain

import androidx.media3.common.MediaItem
import com.kamancho.melisma.app.core.HandleResponse
import com.kamancho.melisma.favorites.presentation.TracksResult
import com.kamancho.melisma.popular.data.PopularRepository
import com.kamancho.melisma.trending.domain.TrackDomain
import javax.inject.Inject

/**
 * Created by HP on 22.08.2023.
 **/
interface PopularInteractor {

    suspend fun update(): TracksResult

    class Base @Inject constructor(
        private val handleResponse: HandleResponse,
        private val repository: PopularRepository,
        private val mapper: TrackDomain.Mapper<MediaItem>
    ): PopularInteractor {

        override suspend fun update(): TracksResult = handleResponse.handle(
            {
                val result = repository.fetchPopulars()
                TracksResult.Success(result.map { it.map(mapper) })
            },{message,_->
                TracksResult.Failure(message)
            }
        )
    }
}