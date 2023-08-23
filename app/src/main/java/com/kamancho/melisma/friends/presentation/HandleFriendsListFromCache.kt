package com.kamancho.melisma.friends.presentation

import com.kamancho.melisma.favorites.presentation.HandleListFromCache
import javax.inject.Inject

/**
 * Created by HP on 18.08.2023.
 **/
interface HandleFriendsListFromCache: HandleListFromCache<FriendUi> {

    class Base @Inject constructor(
        communication: FriendsCommunication
    ): HandleFriendsListFromCache, HandleListFromCache.Abstract<FriendUi>(communication)
}