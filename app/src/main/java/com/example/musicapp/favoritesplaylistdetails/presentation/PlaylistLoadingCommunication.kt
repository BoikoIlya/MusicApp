package com.example.musicapp.favoritesplaylistdetails.presentation

import com.example.musicapp.favorites.presentation.FavoritesTracksLoadingCommunication
import javax.inject.Inject

/**
 * Created by HP on 03.08.2023.
 **/
interface PlaylistLoadingCommunication: FavoritesTracksLoadingCommunication{

    class Base @Inject constructor(): PlaylistLoadingCommunication, FavoritesTracksLoadingCommunication.Base()
}