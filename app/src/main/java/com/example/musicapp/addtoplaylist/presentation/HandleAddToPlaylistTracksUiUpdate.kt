package com.example.musicapp.addtoplaylist.presentation

import androidx.media3.common.MediaItem
import com.example.musicapp.app.core.GlobalSingleUiEventCommunication
import com.example.musicapp.favorites.domain.FavoritesTracksInteractor
import com.example.musicapp.favorites.presentation.FavoritesTracksCommunication
import com.example.musicapp.favorites.presentation.HandlerFavoritesTracksUiUpdate
import com.example.musicapp.favorites.presentation.HandlerFavoritesUiUpdate
import com.example.musicapp.favorites.presentation.TracksResult
import javax.inject.Inject

/**
 * Created by HP on 16.07.2023.
 **/

interface HandleAddToPlaylistTracksUiUpdate: HandlerFavoritesUiUpdate {


    class Base @Inject constructor(
        globalSingleUiEventCommunication: GlobalSingleUiEventCommunication,
        communication: AddToPlaylistCommunication,
        interactor: FavoritesTracksInteractor
    ): HandleAddToPlaylistTracksUiUpdate, HandlerFavoritesUiUpdate.Abstract<SelectedTrackUi, TracksResult>(
        communication,
        globalSingleUiEventCommunication,
        interactor
    )
}