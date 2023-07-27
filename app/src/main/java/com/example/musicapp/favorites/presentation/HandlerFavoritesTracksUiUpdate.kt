package com.example.musicapp.favorites.presentation

import androidx.media3.common.MediaItem
import com.example.musicapp.app.core.GlobalSingleUiEventCommunication
import com.example.musicapp.favorites.domain.FavoritesTracksInteractor
import javax.inject.Inject

/**
 * Created by HP on 09.07.2023.
 **/
interface HandlerFavoritesTracksUiUpdate: HandlerFavoritesUiUpdate {


    class Base @Inject constructor(
        globalSingleUiEventCommunication: GlobalSingleUiEventCommunication,
        favoritesTracksCommunication: FavoritesTracksCommunication,
        interactor: FavoritesTracksInteractor
    ): HandlerFavoritesTracksUiUpdate,HandlerFavoritesUiUpdate.Abstract<MediaItem,TracksResult>(
        favoritesTracksCommunication,
        globalSingleUiEventCommunication,
        interactor
    )
}