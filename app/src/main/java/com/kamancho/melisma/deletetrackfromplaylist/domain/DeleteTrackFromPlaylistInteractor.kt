package com.kamancho.melisma.deletetrackfromplaylist.domain

import com.kamancho.melisma.R
import com.kamancho.melisma.app.core.HandleResponse
import com.kamancho.melisma.app.core.ManagerResource
import com.kamancho.melisma.creteplaylist.data.PlaylistDataRepository
import com.kamancho.melisma.userplaylists.domain.PlaylistsResult
import javax.inject.Inject

/**
 * Created by HP on 19.07.2023.
 **/
interface DeleteTrackFromPlaylistInteractor {

    suspend fun deleteFromPlaylist(playlistId: Int, audioId: Int): PlaylistsResult

    class Base @Inject constructor(
        private val repository: PlaylistDataRepository,
        private val handleResponse: HandleResponse,
        private val managerResource: ManagerResource
    ): DeleteTrackFromPlaylistInteractor{

        override suspend fun deleteFromPlaylist(playlistId: Int,audioId: Int): PlaylistsResult =
            handleResponse.handle({
                repository.removeFromPlaylist(playlistId, listOf(audioId))
                PlaylistsResult.Success(managerResource.getString(R.string.success_remove_message))
            },{message,_->
                PlaylistsResult.Error(message)
            })


    }
}