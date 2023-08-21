package com.example.musicapp.friends.presentation

import com.example.musicapp.favorites.presentation.HandleListFromCache
import javax.inject.Inject

/**
 * Created by HP on 18.08.2023.
 **/
interface HandleFriendsListFromCache: HandleListFromCache<FriendUi> {

    class Base @Inject constructor(
        communication: FriendsCommunication
    ): HandleFriendsListFromCache, HandleListFromCache.Abstract<FriendUi>(communication)
}