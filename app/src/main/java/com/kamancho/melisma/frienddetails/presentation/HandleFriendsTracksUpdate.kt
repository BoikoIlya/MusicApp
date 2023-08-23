package com.kamancho.melisma.frienddetails.presentation

import androidx.media3.common.MediaItem
import com.kamancho.melisma.app.core.GlobalSingleUiEventCommunication
import com.kamancho.melisma.favorites.presentation.FavoritesTracksCommunication
import com.kamancho.melisma.favorites.presentation.HandleUpdate
import com.kamancho.melisma.frienddetails.domain.FriendDetailsPlaylistsInteractor
import com.kamancho.melisma.frienddetails.domain.FriendDetailsTracksInteractor
import com.kamancho.melisma.userplaylists.presentation.PlaylistUi
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