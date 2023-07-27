package com.example.musicapp.selectplaylist.di

import androidx.lifecycle.ViewModel
import com.example.musicapp.favorites.presentation.FavoritesTracksLoadingCommunication
import com.example.musicapp.favorites.presentation.HandleFavoritesListFromCache
import com.example.musicapp.main.di.ViewModelKey
import com.example.musicapp.selectplaylist.presentation.CachedPlaylistsCommunication
import com.example.musicapp.selectplaylist.presentation.HandleFavoritesPlaylistsFromCache
import com.example.musicapp.selectplaylist.presentation.SelectPlaylistCommunication
import com.example.musicapp.selectplaylist.presentation.SelectPlaylistUiStateCommunication
import com.example.musicapp.selectplaylist.presentation.SelectPlaylistViewModel
import com.example.musicapp.userplaylists.data.FetchPlaylistsRepository
import com.example.musicapp.userplaylists.di.PlaylistsScope
import com.example.musicapp.userplaylists.presentation.BaseHandleFavoritesPlaylistsFromCache
import com.example.musicapp.userplaylists.presentation.FavoritesPlaylistCommunication
import com.example.musicapp.userplaylists.presentation.FavoritesPlaylistsUiCommunication
import com.example.musicapp.userplaylists.presentation.HandleFavoritesPlaylistsUiUpdate
import com.example.musicapp.userplaylists.presentation.HandlePlaylistsFetchingFromCache
import com.example.musicapp.userplaylists.presentation.PlaylistUi
import com.example.musicapp.userplaylists.presentation.PlaylistsResultUpdateToUiEventMapper
import com.example.musicapp.userplaylists.presentation.PlaylistsUiStateCommunication
import dagger.Binds
import dagger.Module
import dagger.Reusable
import dagger.multibindings.IntoMap

/**
 * Created by HP on 01.05.2023.
 **/
@Module
interface SelectPlaylistModule {

    @Binds
    @Reusable
    fun bindFavoritesPlaylistCommunication(obj: FavoritesPlaylistCommunication.Base): FavoritesPlaylistCommunication

    @Binds
    @Reusable
    fun bindFavoritesTracksLoadingCommunication(obj: FavoritesTracksLoadingCommunication.Base): FavoritesTracksLoadingCommunication

    @Binds
    @Reusable
    fun bindPlaylistsUiStateCommunication(obj: PlaylistsUiStateCommunication.Base): PlaylistsUiStateCommunication

    @Binds
    @SelectPlaylistScope
    fun bindFavoritesPlaylistsUiCommuncation(obj: FavoritesPlaylistsUiCommunication.Base): FavoritesPlaylistsUiCommunication

//    @Binds
//    @SelectPlaylistScope
//    fun bindBaseHandleFavoritesPlaylistsFromCache(obj: BaseHandleFavoritesPlaylistsFromCache): HandleFavoritesListFromCache<PlaylistUi>

    @Binds
    @SelectPlaylistScope
    fun bindHandlePlaylistsFetchingFromCache(obj: HandlePlaylistsFetchingFromCache.Base): HandlePlaylistsFetchingFromCache

    @Binds
    @SelectPlaylistScope
    fun bindFetchPlaylistsRepository(obj: FetchPlaylistsRepository.Base): FetchPlaylistsRepository

    @Binds
    @SelectPlaylistScope
    fun bindPlaylistsResultUpdateToUiEventMapper(obj: PlaylistsResultUpdateToUiEventMapper.Base): PlaylistsResultUpdateToUiEventMapper

    @Binds
    @SelectPlaylistScope
    fun bindHandleFavoritesPlaylistsFromCache(obj: HandleFavoritesPlaylistsFromCache.Base): HandleFavoritesPlaylistsFromCache

    @Binds
    @SelectPlaylistScope
    fun bindHandleFavoritesPlaylistsUiUpdate(obj: HandleFavoritesPlaylistsUiUpdate.BaseForSelectPlaylist): HandleFavoritesPlaylistsUiUpdate

    @Binds
    @SelectPlaylistScope
    fun bindPlaylistUiToIddMapper(obj: PlaylistUi.ToIdMapper): PlaylistUi.Mapper<Int>

    @Binds
    @SelectPlaylistScope
    fun bindCachedPlaylistsCommunication(obj: CachedPlaylistsCommunication.Base): CachedPlaylistsCommunication

    @Binds
    @SelectPlaylistScope
    fun bindSelectPlaylistCommunication(obj: SelectPlaylistCommunication.Base): SelectPlaylistCommunication

    @Binds
    @SelectPlaylistScope
    fun bindSelectPlaylistPlaylistUiStateCommunication(obj: SelectPlaylistUiStateCommunication.Base): SelectPlaylistUiStateCommunication



    @Binds
    @[IntoMap ViewModelKey(SelectPlaylistViewModel::class)]
    fun bindSelectPlaylistViewModel(selectPlaylistViewModel: SelectPlaylistViewModel): ViewModel

}
