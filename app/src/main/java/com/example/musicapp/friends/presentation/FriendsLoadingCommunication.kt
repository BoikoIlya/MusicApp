package com.example.musicapp.friends.presentation

import com.example.musicapp.favorites.presentation.FavoritesTracksLoadingCommunication
import javax.inject.Inject

/**
 * Created by HP on 03.08.2023.
 **/
interface FriendsLoadingCommunication: FavoritesTracksLoadingCommunication{

    class Base @Inject constructor(): FriendsLoadingCommunication, FavoritesTracksLoadingCommunication.Base()
}