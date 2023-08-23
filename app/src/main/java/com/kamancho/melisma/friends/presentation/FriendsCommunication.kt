package com.kamancho.melisma.friends.presentation

import com.kamancho.melisma.favorites.presentation.FavoritesUiCommunication
import javax.inject.Inject

/**
 * Created by HP on 23.05.2023.
 **/
interface FriendsCommunication: FavoritesUiCommunication<FriendUi>{

    class Base @Inject constructor(
        friendsListCommunication: FriendsListCommunication,
        friendsStateCommunication: FriendsStateCommunication,
        friendsLoadingCommunication: FriendsLoadingCommunication,
    ): FriendsCommunication, FavoritesUiCommunication.Abstract<FriendUi>(
       friendsStateCommunication,friendsListCommunication,friendsLoadingCommunication
    )

}


