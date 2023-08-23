package com.kamancho.melisma.favorites.presentation

import androidx.media3.common.MediaItem
import com.kamancho.melisma.app.core.GlobalSingleUiEventCommunication
import com.kamancho.melisma.favorites.domain.FavoritesTracksInteractor
import javax.inject.Inject

/**
 * Created by HP on 09.07.2023.
 **/
interface HandlerFavoritesTracksUiUpdate: HandleUpdate {


    class Base @Inject constructor(
        globalSingleUiEventCommunication: GlobalSingleUiEventCommunication,
        favoritesTracksCommunication: FavoritesTracksCommunication,
        interactor: FavoritesTracksInteractor
    ): HandlerFavoritesTracksUiUpdate,HandleUpdate.Abstract<MediaItem>(
        favoritesTracksCommunication,
        globalSingleUiEventCommunication,
        interactor
    )
}