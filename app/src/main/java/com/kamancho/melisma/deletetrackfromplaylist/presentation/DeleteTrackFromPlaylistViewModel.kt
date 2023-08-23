package com.kamancho.melisma.deletetrackfromplaylist.presentation

import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import com.kamancho.melisma.app.core.DataTransfer
import com.kamancho.melisma.app.core.DispatchersList
import com.kamancho.melisma.deletetrackfromplaylist.domain.DeleteTrackFromPlaylistInteractor
import com.kamancho.melisma.favorites.presentation.DialogViewModel
import com.kamancho.melisma.favorites.presentation.ResetSwipeActionCommunication
import com.kamancho.melisma.trending.domain.TrackDomain
import com.kamancho.melisma.userplaylists.domain.PlaylistDomain
import com.kamancho.melisma.userplaylists.presentation.PlaylistsResultUpdateToUiEventMapper
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