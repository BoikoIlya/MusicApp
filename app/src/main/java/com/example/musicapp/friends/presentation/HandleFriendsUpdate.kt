package com.example.musicapp.friends.presentation

import com.example.musicapp.app.core.GlobalSingleUiEventCommunication
import com.example.musicapp.app.core.UpdateInteractor
import com.example.musicapp.favorites.presentation.FavoritesUiCommunication
import com.example.musicapp.favorites.presentation.HandleUpdate
import com.example.musicapp.friends.domain.FriendsInteractor
import javax.inject.Inject

/**
 * Created by HP on 18.08.2023.
 **/
interface HandleFriendsUpdate: HandleUpdate {

    class Base @Inject constructor(
        uiStateCommunication: FriendsCommunication,
        globalSingleUiEventCommunication: GlobalSingleUiEventCommunication,
        interactor: FriendsInteractor
    ):  HandleUpdate.Abstract<FriendUi>(
        uiStateCommunication,globalSingleUiEventCommunication,interactor
    ),HandleFriendsUpdate
}