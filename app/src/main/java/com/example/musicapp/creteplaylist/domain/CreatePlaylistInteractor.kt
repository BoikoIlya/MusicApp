package com.example.musicapp.app.core

import androidx.media3.common.MediaItem
import com.example.musicapp.R
import com.example.musicapp.addtoplaylist.domain.SelectedTrackDomain
import com.example.musicapp.addtoplaylist.presentation.SelectedTrackUi
import com.example.musicapp.app.core.ToMediaItemMapper.Companion.track_id
import com.example.musicapp.creteplaylist.data.PlaylistDataRepository
import com.example.musicapp.creteplaylist.presentation.SelectedTracksStore
import com.example.musicapp.trending.domain.TrackDomain
import com.example.musicapp.userplaylists.domain.PlaylistsResult
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

    suspend fun followPlaylist(playlistId: Int,playlistOwnerId: Int): PlaylistsResult

}

class PlaylistDataInteractor @Inject constructor(
    private val handleResponse: HandleResponse,
    private val repository: PlaylistDataRepository,
    private val managerResource: ManagerResource,
    private val store: SelectedTracksStore,
    private val toTrackIdMapper: SelectedTrackDomain.Mapper<Int>,
    private val toTrackDomain: SelectedTrackUi.Mapper<SelectedTrackDomain>
): EditPlaylistInteractor,CreatePlaylistInteractor,FollowPlaylistInteractor{

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
    },{message,_->
        PlaylistsResult.Error(message)
    })

    override suspend fun createPlaylist(
        title: String,
        description: String,
    ): PlaylistsResult = handleResponse.handle({
        val selectedTracks = store.read()
        repository.createPlaylist(title, description,selectedTracks.map { it.map(toTrackIdMapper) })
        PlaylistsResult.Success(managerResource.getString(R.string.success_create_message))
    },{message,_->
        PlaylistsResult.Error(message)
    })

    override suspend fun followPlaylist(
        playlistId: Int,
        playlistOwnerId: Int,
    ): PlaylistsResult = handleResponse.handle({
        repository.followPlaylist(playlistId, playlistOwnerId)
        PlaylistsResult.Success(managerResource.getString(R.string.success_followed_message))
    },{message,_->
        PlaylistsResult.Error(message)
    })


}