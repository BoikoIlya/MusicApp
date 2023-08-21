package com.example.musicapp.friends.presentation

import androidx.lifecycle.LifecycleOwner
import androidx.media3.common.MediaItem
import com.example.musicapp.favorites.presentation.FavoritesUiCommunication
import com.example.musicapp.userplaylists.presentation.PlaylistUi
import kotlinx.coroutines.flow.FlowCollector
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


