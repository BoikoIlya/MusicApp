package com.example.musicapp.favorites.domain

import androidx.media3.common.MediaItem
import com.example.musicapp.app.core.DataTransfer
import com.example.musicapp.app.core.FavoritesInteractor
import com.example.musicapp.app.core.HandleResponse
import com.example.musicapp.app.core.ManagerResource
import com.example.musicapp.app.core.MediaItemToTrackDomainMapper
import com.example.musicapp.app.vkdto.TrackItem
import com.example.musicapp.favorites.data.SortingState
import com.example.musicapp.favorites.data.FavoritesTracksRepository
import com.example.musicapp.favorites.data.cache.TrackCache
import com.example.musicapp.favorites.presentation.TracksResult
import com.example.musicapp.trending.domain.TrackDomain
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
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