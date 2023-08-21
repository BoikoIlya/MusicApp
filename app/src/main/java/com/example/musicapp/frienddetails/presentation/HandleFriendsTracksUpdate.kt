package com.example.musicapp.frienddetails.presentation

import androidx.media3.common.MediaItem
import com.example.musicapp.app.core.GlobalSingleUiEventCommunication
import com.example.musicapp.app.core.UpdateInteractor
import com.example.musicapp.favorites.presentation.FavoritesTracksCommunication
import com.example.musicapp.favorites.presentation.FavoritesUiCommunication
import com.example.musicapp.favorites.presentation.HandleUpdate
import com.example.musicapp.frienddetails.domain.FriendDetailsPlaylistsInteractor
import com.example.musicapp.frienddetails.domain.FriendDetailsTracksInteractor
import com.example.musicapp.friends.domain.FriendsInteractor
import com.example.musicapp.userplaylists.presentation.FavoritesPlaylistCommunication
import com.example.musicapp.userplaylists.presentation.FavoritesPlaylistsUiCommunication
import com.example.musicapp.userplaylists.presentation.PlaylistUi
import com.example.musicapp.userplaylists.presentation.PlaylistsUiStateCommunication
import javax.inject.Inject

/**
 * Created by HP on 18.08.2023.
 **/
interface HandleFriendsTracksUpdate: HandleUpdate {

    class Base @Inject constructor(
        favoritesTracksCommunication: FavoritesTracksCommunication,
        globalSingleUiEventCommunication: GlobalSingleUiEventCommunication,
        interactor: FriendDetailsTracksInteractor
    ):  HandleUpdate.Abstract<MediaItem>(
        favoritesTracksCommunication,globalSingleUiEventCommunication,interactor
    ),HandleFriendsTracksUpdate
}

interface HandleFriendsPlaylistsUpdate: HandleUpdate {

    class Base @Inject constructor(
        favoritesPlaylistCommunication: FriendPlaylistsCommunication,
        globalSingleUiEventCommunication: GlobalSingleUiEventCommunication,
        interactor: FriendDetailsPlaylistsInteractor
    ):  HandleUpdate.Abstract<PlaylistUi>(
        favoritesPlaylistCommunication,globalSingleUiEventCommunication,interactor
    ),HandleFriendsPlaylistsUpdate
}