package com.example.musicapp.selectplaylist.domain

import com.example.musicapp.R
import com.example.musicapp.app.core.HandleResponse
import com.example.musicapp.app.core.ManagerResource
import com.example.musicapp.creteplaylist.data.PlaylistDataRepository
import com.example.musicapp.selectplaylist.data.AddTrackToPlaylistRepository
import com.example.musicapp.userplaylists.domain.PlaylistsResult
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