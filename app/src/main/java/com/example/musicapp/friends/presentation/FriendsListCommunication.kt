package com.example.musicapp.friends.presentation

import androidx.media3.common.MediaItem
import com.example.musicapp.app.core.Communication
import javax.inject.Inject

/**
 * Created by HP on 23.05.2023.
 **/
interface FriendsListCommunication: Communication.Mutable<List<FriendUi>>{
    class Base @Inject constructor():
        Communication.UiUpdate<List<FriendUi>>(emptyList()),
        FriendsListCommunication
}