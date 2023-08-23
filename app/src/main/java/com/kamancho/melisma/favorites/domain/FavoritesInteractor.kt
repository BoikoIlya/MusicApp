package com.kamancho.melisma.favorites.domain

import androidx.media3.common.MediaItem
import com.kamancho.melisma.app.core.DataTransfer
import com.kamancho.melisma.app.core.FavoritesInteractor
import com.kamancho.melisma.app.core.HandleResponse
import com.kamancho.melisma.app.core.ManagerResource
import com.kamancho.melisma.app.core.MediaItemToTrackDomainMapper
import com.kamancho.melisma.app.vkdto.TrackItem
import com.kamancho.melisma.favorites.data.FavoritesTracksRepository
import com.kamancho.melisma.favorites.data.cache.TrackCache
import com.kamancho.melisma.favorites.presentation.TracksResult
import com.kamancho.melisma.trending.domain.TrackDomain
import javax.inject.Inject

/**
 * Created by HP on 17.06.2023.
 **/


interface FavoritesTracksInteractor: FavoritesInteractor<TrackDomain,TrackCache,TrackItem,MediaItem,TracksResult>{


    class Base @Inject constructor(
        private val repository: FavoritesTracksRepository,
        handleResponse: HandleResponse,
        managerResource: ManagerResource,
        uiToDomainMapper: MediaItemToTrackDomainMapper,
        transfer: DataTransfer<TrackDomain>
    ): FavoritesInteractor.Abstract<TrackDomain,TrackCache,TrackItem,MediaItem,TracksResult>(
     repository, managerResource, uiToDomainMapper, handleResponse, transfer
    ),FavoritesTracksInteractor{
        override fun success(message: String, newId: Int): TracksResult =
            TracksResult.Success(message = message,newId = newId)

        override fun error(message: String): TracksResult = TracksResult.Failure(message)

        override fun duplicated(): TracksResult = TracksResult.Duplicate

    }


}