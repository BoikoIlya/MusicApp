package com.example.musicapp.app.core

import android.util.Log
import androidx.media3.common.MediaItem
import com.example.musicapp.R
import com.example.musicapp.addtoplaylist.domain.SelectedTrackDomain
import com.example.musicapp.addtoplaylist.presentation.SelectedTrackUi
import com.example.musicapp.creteplaylist.data.PlaylistDataRepository
import com.example.musicapp.creteplaylist.presentation.SelectedTracksStore
import com.example.musicapp.trending.domain.TrackDomain
import com.example.musicapp.userplaylists.domain.PlaylistDomain
import com.example.musicapp.userplaylists.domain.PlaylistsResult
import com.example.musicapp.userplaylists.presentation.PlaylistUi
import javax.inject.Inject

/**
 * Created by HP on 17.07.2023.
 **/
interface EditPlaylistInteractor {

    suspend fun editPlaylist(
        playlistId: Int,
        title: String,
        description: String,
        initialTracks: List<SelectedTrackUi>,
    ): PlaylistsResult
}

interface CreatePlaylistInteractor {

    suspend fun createPlaylist(
        title: String,
        description: String,
    ): PlaylistsResult
}

interface FollowPlaylistInteractor{

    suspend fun followPlaylist(playlist: PlaylistUi): PlaylistsResult

}


interface PlaylistDataInteractor: EditPlaylistInteractor,CreatePlaylistInteractor,FollowPlaylistInteractor {

    class Base @Inject constructor(
        private val handleResponse: HandleResponse,
        private val repository: PlaylistDataRepository,
        private val managerResource: ManagerResource,
        private val store: SelectedTracksStore,
        private val toTrackIdMapper: SelectedTrackDomain.Mapper<Int>,
        private val toTrackDomain: SelectedTrackUi.Mapper<SelectedTrackDomain>,
        private val toPlaylistDomainMapper: PlaylistUi.Mapper<PlaylistDomain>
    ) : PlaylistDataInteractor
    {

        override suspend fun editPlaylist(
            playlistId: Int,
            title: String,
            description: String,
            initialTracks: List<SelectedTrackUi>,
        ): PlaylistsResult = handleResponse.handle({
            val initialTracksDomain = initialTracks.map { it.map(toTrackDomain) }.toSet()
            repository.editPlaylist(
                playlistId,
                title,
                description,
                store.read().subtract(initialTracksDomain).map { it.map(toTrackIdMapper) },
                initialTracksDomain.subtract(store.read().toSet()).map { it.map(toTrackIdMapper) }
            )
            PlaylistsResult.Success(managerResource.getString(R.string.success_edit_message))
        }, { message, _ ->
            PlaylistsResult.Error(message)
        })

        override suspend fun createPlaylist(
            title: String,
            description: String,
        ): PlaylistsResult = handleResponse.handle({
            val selectedTracks = store.read()
            repository.createPlaylist(
                title,
                description,
                selectedTracks.map { it.map(toTrackIdMapper) })
            PlaylistsResult.Success(managerResource.getString(R.string.success_create_message))
        }, { message, _ ->
            PlaylistsResult.Error(message)
        })

        override suspend fun followPlaylist(
            playlist: PlaylistUi
        ): PlaylistsResult = handleResponse.handle({
            repository.followPlaylist(playlist.map(toPlaylistDomainMapper))
            PlaylistsResult.Success(managerResource.getString(R.string.success_followed_message))
        }, { message, e ->
            PlaylistsResult.Error(message)
        })


    }
}