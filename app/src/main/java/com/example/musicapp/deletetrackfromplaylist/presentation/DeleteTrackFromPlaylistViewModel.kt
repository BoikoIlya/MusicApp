package com.example.musicapp.deletetrackfromplaylist.presentation

import android.util.Log
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import com.example.musicapp.app.core.DataTransfer
import com.example.musicapp.app.core.DispatchersList
import com.example.musicapp.deletetrackfromplaylist.domain.DeleteTrackFromPlaylistInteractor
import com.example.musicapp.favorites.data.cache.TrackDomainToCacheMapper
import com.example.musicapp.favorites.presentation.DialogViewModel
import com.example.musicapp.favorites.presentation.ResetSwipeActionCommunication
import com.example.musicapp.trending.domain.TrackDomain
import com.example.musicapp.userplaylists.domain.PlaylistDomain
import com.example.musicapp.userplaylists.domain.PlaylistsResult
import com.example.musicapp.userplaylists.presentation.PlaylistsResultUpdateToUiEventMapper
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by HP on 11.08.2023.
 **/
class DeleteTrackFromPlaylistViewModel @Inject constructor(
    transfer: DataTransfer<TrackDomain>,
    private val playlistTransfer: DataTransfer<PlaylistDomain>,
    private val toPlaylistIdMapper: PlaylistDomain.Mapper<String>,
    private val interactor: DeleteTrackFromPlaylistInteractor,
    private val resetSwipeActionCommunication: ResetSwipeActionCommunication,
    private val dispatchersList: DispatchersList,
    private val mapper: TrackDomain.Mapper<MediaItem>,
    private val playlistResultMapper: PlaylistsResultUpdateToUiEventMapper
): DialogViewModel<TrackDomain>(transfer) {

    fun deleteTrackFromPlaylist() = viewModelScope.launch(dispatchersList.io()) {
        val item = super.fetchData()!!.map(mapper)
        interactor.deleteFromPlaylist(
            playlistTransfer.read()!!.map(toPlaylistIdMapper).toInt(),
            item.mediaId.toInt()
        ).map(playlistResultMapper)
    }

    fun resetSwipedItem() = viewModelScope.launch(dispatchersList.io()) {
        resetSwipeActionCommunication.map(Unit)
    }
}