package com.kamancho.melisma.selectplaylist.domain

import com.kamancho.melisma.R
import com.kamancho.melisma.app.core.HandleResponse
import com.kamancho.melisma.app.core.ManagerResource
import com.kamancho.melisma.selectplaylist.data.AddTrackToPlaylistRepository
import com.kamancho.melisma.userplaylists.domain.PlaylistsResult
import javax.inject.Inject

/**
 * Created by HP on 19.07.2023.
 **/
interface AddTrackToPlaylistInteractor {

    suspend fun addTrackToPlaylist(playlistId: Int, audioId: Int): PlaylistsResult

    class Base @Inject constructor(
        private val repository: AddTrackToPlaylistRepository,
        private val handleResponse: HandleResponse,
        private val managerResource: ManagerResource
    ): AddTrackToPlaylistInteractor{

        override suspend fun addTrackToPlaylist(playlistId: Int,audioId: Int): PlaylistsResult =
            handleResponse.handle({
                repository.addToPlaylist(playlistId, audioId)
                PlaylistsResult.Success(managerResource.getString(R.string.success_add_message))
            },{message,_->
                PlaylistsResult.Error(message)
            })


    }
}