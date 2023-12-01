package com.kamancho.melisma.favoritesplaylistdetails.presentation

import com.kamancho.melisma.app.core.DispatchersList
import com.kamancho.melisma.userplaylists.data.FetchPlaylistsRepository
import com.kamancho.melisma.userplaylists.domain.PlaylistDomain
import com.kamancho.melisma.userplaylists.presentation.PlaylistUi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by HP on 05.08.2023.
 **/
interface HandlePlaylistDataCache {

    fun init(playlistUi: PlaylistUi)

    fun handle(
        scope: CoroutineScope,
        playlistId: String
    )

    class Base @Inject constructor(
        private val fetchPlaylistsRepository: FetchPlaylistsRepository,
        private val dispatchersList: DispatchersList,
        private val mapper: PlaylistDomain.Mapper<PlaylistUi>,
        private val isItemInvalidMapper: PlaylistDomain.Mapper<Boolean>,
        private val playlistDataCommunication: PlaylistDataCommunication
    ):HandlePlaylistDataCache {

        override fun init(playlistUi: PlaylistUi) = playlistDataCommunication.map(playlistUi)

        override fun handle(
            scope: CoroutineScope,
            playlistId: String
        ) {
            scope.launch(dispatchersList.io()) {
                fetchPlaylistsRepository.getPlaylistById(playlistId).collect{
                    if(!it.map(isItemInvalidMapper))
                        playlistDataCommunication.map(it.map(mapper))

                }
            }
        }
    }
}