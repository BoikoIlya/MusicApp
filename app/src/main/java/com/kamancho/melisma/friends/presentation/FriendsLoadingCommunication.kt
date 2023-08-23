package com.kamancho.melisma.friends.presentation

import com.kamancho.melisma.favorites.presentation.FavoritesTracksLoadingCommunication
import javax.inject.Inject

/**
 * Created by HP on 03.08.2023.
 **/
interface FriendsLoadingCommunication: FavoritesTracksLoadingCommunication{

    class Base @Inject constructor(): FriendsLoadingCommunication, FavoritesTracksLoadingCommunication.Base()
}