package com.kamancho.melisma.userplaylists.domain

import com.kamancho.melisma.app.core.DataTransfer
import com.kamancho.melisma.app.core.FavoritesInteractor
import com.kamancho.melisma.app.core.HandleResponse
import com.kamancho.melisma.app.core.ManagerResource
import com.kamancho.melisma.app.vkdto.PlaylistItem
import com.kamancho.melisma.userplaylists.data.FavoritesPlaylistsRepository
import com.kamancho.melisma.userplaylists.data.cache.PlaylistCache
import com.kamancho.melisma.userplaylists.presentation.PlaylistUi
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