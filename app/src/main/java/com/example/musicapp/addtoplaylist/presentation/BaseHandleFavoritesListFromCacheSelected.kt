package com.example.musicapp.addtoplaylist.presentation

import android.util.Log
import androidx.media3.common.MediaItem
import com.example.musicapp.addtoplaylist.domain.SelectedTrackDomain
import com.example.musicapp.addtoplaylist.domain.SelectedTracksInteractor
import com.example.musicapp.favorites.presentation.HandleFavoritesListFromCache
import com.example.musicapp.creteplaylist.presentation.SelectedTracksStore
import javax.inject.Inject

/**
 * Created by HP on 16.07.2023.
 **/

//interface HandleFavoritesListFromCacheSelected: HandleFavoritesListFromCache<SelectedTrackUi> {
//
//    class Base @Inject constructor(
//        private val selectedTracksInteractor: SelectedTracksInteractor,
//        addToPlaylistCommunication: AddToPlaylistCommunication
//    ) : HandleFavoritesListFromCacheSelected,
//        HandleFavoritesListFromCache.Abstract<SelectedTrackUi>(addToPlaylistCommunication) {
//
//        override suspend fun handle(list: List<SelectedTrackUi>) {
//            super.handle(selectedTracksInteractor.map(list))
//        }
//    }
//
//}