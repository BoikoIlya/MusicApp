package com.kamancho.melisma.addtoplaylist.presentation

import com.kamancho.melisma.app.core.GlobalSingleUiEventCommunication
import com.kamancho.melisma.favorites.domain.FavoritesTracksInteractor
import com.kamancho.melisma.favorites.presentation.HandleUpdate
import javax.inject.Inject

/**
 * Created by HP on 16.07.2023.
 **/

interface HandleAddToPlaylistTracksUiUpdate: HandleUpdate {


    class Base @Inject constructor(
        globalSingleUiEventCommunication: GlobalSingleUiEventCommunication,
        communication: AddToPlaylistCommunication,
        interactor: FavoritesTracksInteractor
    ): HandleAddToPlaylistTracksUiUpdate, HandleUpdate.Abstract<SelectedTrackUi>(
        communication,
        globalSingleUiEventCommunication,
        interactor
    )
}