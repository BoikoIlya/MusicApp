package com.example.musicapp.userplaylists.domain

import com.example.musicapp.app.core.DataTransfer
import com.example.musicapp.app.core.FavoritesInteractor
import com.example.musicapp.app.core.HandleResponse
import com.example.musicapp.app.core.ManagerResource
import com.example.musicapp.app.vkdto.PlaylistItem
import com.example.musicapp.userplaylists.data.FavoritesPlaylistsRepository
import com.example.musicapp.userplaylists.data.cache.PlaylistCache
import com.example.musicapp.userplaylists.presentation.PlaylistUi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * Created by HP on 13.07.2023.
 **/
interface FavoritesPlaylistsInteractor: FavoritesInteractor<PlaylistDomain,PlaylistCache,PlaylistItem,PlaylistUi,PlaylistsResult> {

    class Base @Inject constructor(
        repository: FavoritesPlaylistsRepository,
        managerResource: ManagerResource,
        uiToDomainMapper: PlaylistUiToDomainMapper,
        handlerResponse: HandleResponse,
        transfer: DataTransfer<PlaylistDomain>,
    ): FavoritesPlaylistsInteractor,
        FavoritesInteractor.Abstract<PlaylistDomain,PlaylistCache,PlaylistItem,PlaylistUi,PlaylistsResult>(
            repository,
            managerResource,
            uiToDomainMapper,
            handlerResponse,
            transfer
    ) {

        override fun success(message: String, newId: Int): PlaylistsResult = PlaylistsResult.Success(message)

        override fun error(message: String): PlaylistsResult = PlaylistsResult.Error(message)

        override fun duplicated(): PlaylistsResult = PlaylistsResult.Duplicated

    }
}