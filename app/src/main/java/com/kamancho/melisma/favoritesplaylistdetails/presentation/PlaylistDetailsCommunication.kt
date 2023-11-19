package com.kamancho.melisma.favoritesplaylistdetails.presentation

import androidx.lifecycle.LifecycleOwner
import androidx.media3.common.MediaItem
import com.kamancho.melisma.favorites.presentation.FavoritesUiCommunication
import com.kamancho.melisma.userplaylists.presentation.PlaylistUi
import kotlinx.coroutines.flow.FlowCollector
import javax.inject.Inject

/**
 * Created by HP on 23.05.2023.
 **/
interface PlaylistDetailsCommunication: FavoritesUiCommunication<MediaItem>{

    fun showPlaylistData(playlistUi: PlaylistUi)

    suspend fun collectPlaylistDetails(owner: LifecycleOwner, collector: FlowCollector<PlaylistUi>)
//
//    fun showAddBtn(visibility: Int)
//
//    suspend fun collectAddBtnVisibility(owner: LifecycleOwner, collector: FlowCollector<Int>)

    class Base @Inject constructor(
        playlistTracksCommunication: PlaylistDetailsTracksCommunication,
        playlistStateCommunication: PlaylistDetailsStateCommunication,
        favoritesTracksLoadingCommunication: PlaylistLoadingCommunication,
        private val playlistDataCommunication: PlaylistDataCommunication,
      //  private val addBtnVisibilityCommunication: AddBtnVisibilityCommunication
    ): PlaylistDetailsCommunication, FavoritesUiCommunication.Abstract<MediaItem>(
        playlistStateCommunication,
        playlistTracksCommunication,
        favoritesTracksLoadingCommunication
    ) {
        override fun showPlaylistData(playlistUi: PlaylistUi) {
            playlistDataCommunication.map(playlistUi)
        }

        override suspend fun collectPlaylistDetails(
            owner: LifecycleOwner,
            collector: FlowCollector<PlaylistUi>,
        ) = playlistDataCommunication.collect(owner,collector)

//        override fun showAddBtn(visibility: Int) = addBtnVisibilityCommunication.map(visibility)
//
//        override suspend fun collectAddBtnVisibility(
//            owner: LifecycleOwner,
//            collector: FlowCollector<Int>,
//        ) = addBtnVisibilityCommunication.collect(owner,collector)
    }

}


