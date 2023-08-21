package com.example.musicapp.addtoplaylist.presentation

import com.example.musicapp.app.core.GlobalSingleUiEventCommunication
import com.example.musicapp.favorites.domain.FavoritesTracksInteractor
import com.example.musicapp.favorites.presentation.HandleUpdate
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