package com.example.musicapp.userplaylists.di

import androidx.lifecycle.ViewModel
import com.example.musicapp.favorites.presentation.FavoritesTracksLoadingCommunication
import com.example.musicapp.favorites.presentation.HandleFavoritesListFromCache
import com.example.musicapp.main.di.ViewModelKey
import com.example.musicapp.userplaylists.data.FetchPlaylistsRepository
import com.example.musicapp.userplaylists.presentation.BaseHandleFavoritesPlaylistsFromCache
import com.example.musicapp.userplaylists.presentation.CanEditPlaylistStateCommunication
import com.example.musicapp.userplaylists.presentation.FavoritesPlaylistCommunication
import com.example.musicapp.userplaylists.presentation.FavoritesPlaylistsUiCommunication
import com.example.musicapp.userplaylists.presentation.HandleFavoritesPlaylistsUiUpdate
import com.example.musicapp.userplaylists.presentation.HandlePlaylistsFetchingFromCache
import com.example.musicapp.userplaylists.presentation.PlaylistUi
import com.example.musicapp.userplaylists.presentation.PlaylistsMenuDialogBottomSheetViewModel
import com.example.musicapp.userplaylists.presentation.PlaylistsResultUpdateToUiEventMapper
import com.example.musicapp.userplaylists.presentation.PlaylistsUiStateCommunication
import com.example.musicapp.userplaylists.presentation.PlaylistsViewModel
import dagger.Binds
import dagger.Module
import dagger.Reusable
import dagger.multibindings.IntoMap

/**
 * Created by HP on 28.01.2023.
 **/


@Module
interface PlaylistsModule{


    @Binds
    @PlaylistsScope
    fun bindHandleFavoritesPlaylistsUiUpdate(obj: HandleFavoritesPlaylistsUiUpdate.BaseForFavoritesPlaylists):HandleFavoritesPlaylistsUiUpdate

    @Binds
    @PlaylistsScope
    fun bindCanEditPlaylistStateCommunication(obj: CanEditPlaylistStateCommunication.Base):CanEditPlaylistStateCommunication

    @Binds
    @PlaylistsScope
    fun bindPlaylistUiCanEdit(obj: PlaylistUi.CanEdit):PlaylistUi.Mapper<Boolean>

    @Binds
    @PlaylistsScope
    fun bindBaseHandleFavoritesPlaylistsFromCache(obj: BaseHandleFavoritesPlaylistsFromCache):HandleFavoritesListFromCache<PlaylistUi>

    @Binds
    @PlaylistsScope
    fun bindPlaylistsResultUpdateToUiEventMapper(obj: PlaylistsResultUpdateToUiEventMapper.Base): PlaylistsResultUpdateToUiEventMapper


    @Binds
    @PlaylistsScope
    fun bindFavoritesPlaylistCommunication(obj: FavoritesPlaylistCommunication.Base): FavoritesPlaylistCommunication

    @Binds
    @PlaylistsScope
    fun bindPlaylistsUiStateCommunication(obj: PlaylistsUiStateCommunication.Base):PlaylistsUiStateCommunication

    @Binds
    @Reusable
    fun bindFavoritesTracksLoadingCommunication(obj: FavoritesTracksLoadingCommunication.Base):FavoritesTracksLoadingCommunication

    @Binds
    @PlaylistsScope
    fun bindFavoritesPlaylistsUiCommuncation(obj: FavoritesPlaylistsUiCommunication.Base):FavoritesPlaylistsUiCommunication

    @Binds
    @PlaylistsScope
    fun bindPlaylistUiToIdMapper(obj: PlaylistUi.ToIdMapper): PlaylistUi.Mapper<Int>

    @Binds
    @PlaylistsScope
    fun bindHandlePlaylistsFetchingFromCache(obj: HandlePlaylistsFetchingFromCache.Base): HandlePlaylistsFetchingFromCache

    @Binds
    @PlaylistsScope
    fun bindFetchPlaylistsRepository(obj: FetchPlaylistsRepository.Base): FetchPlaylistsRepository


    @Binds
    @[IntoMap ViewModelKey(PlaylistsViewModel::class)]
    fun bindPlaylistsViewModel(playlistsViewModel: PlaylistsViewModel): ViewModel


    @Binds
    @[IntoMap ViewModelKey(PlaylistsMenuDialogBottomSheetViewModel::class)]
    fun bindPlaylistsMenuDialogBottomSheetViewModel(playlistsMenuDialogBottomSheetViewModel: PlaylistsMenuDialogBottomSheetViewModel): ViewModel

}

