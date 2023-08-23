package com.kamancho.melisma.friends.presentation

import com.kamancho.melisma.app.core.Communication
import javax.inject.Inject

/**
 * Created by HP on 23.05.2023.
 **/
interface FriendsListCommunication: Communication.Mutable<List<FriendUi>>{
    class Base @Inject constructor():
        Communication.UiUpdate<List<FriendUi>>(emptyList()),
        FriendsListCommunication
}