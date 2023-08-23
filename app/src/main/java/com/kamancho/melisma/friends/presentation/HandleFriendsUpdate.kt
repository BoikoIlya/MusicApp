package com.kamancho.melisma.friends.presentation

import com.kamancho.melisma.app.core.GlobalSingleUiEventCommunication
import com.kamancho.melisma.favorites.presentation.HandleUpdate
import com.kamancho.melisma.friends.domain.FriendsInteractor
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