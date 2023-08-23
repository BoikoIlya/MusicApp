package com.kamancho.melisma.favoritesplaylistdetails.presentation

import com.kamancho.melisma.favorites.presentation.FavoritesTracksLoadingCommunication
import javax.inject.Inject

/**
 * Created by HP on 03.08.2023.
 **/
interface PlaylistLoadingCommunication: FavoritesTracksLoadingCommunication{

    class Base @Inject constructor(): PlaylistLoadingCommunication, FavoritesTracksLoadingCommunication.Base()
}