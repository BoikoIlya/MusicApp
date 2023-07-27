package com.example.musicapp.addtoplaylist.di

import androidx.lifecycle.ViewModel
import com.example.musicapp.addtoplaylist.presentation.AddToPlaylistCommunication
import com.example.musicapp.addtoplaylist.presentation.AddToPlaylistUiStateCommunication
import com.example.musicapp.addtoplaylist.presentation.AddToPlaylistViewModel
import com.example.musicapp.addtoplaylist.presentation.CachedTracksCommunication
import com.example.musicapp.addtoplaylist.presentation.HandleAddToPlaylistTracksUiUpdate

import com.example.musicapp.favorites.presentation.FavoritesTracksLoadingCommunication
import com.example.musicapp.main.di.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

/**
 * Created by HP on 16.07.2023.
 **/
@Module
interface AddToPlaylistModule {

//    @Binds
//    @AddToPlaylistScope
//    fun bindHandleSelectedTracksSortedSearch(obj: HandleSelectedTracksSortedSearch.Base): HandleSelectedTracksSortedSearch

    @Binds
    @AddToPlaylistScope
    fun bindFavoritesTracksLoadingCommunication(obj: FavoritesTracksLoadingCommunication.Base): FavoritesTracksLoadingCommunication

    @Binds
    @AddToPlaylistScope
    fun bindHandleAddToPlaylistTracksUiUpdate(obj: HandleAddToPlaylistTracksUiUpdate.Base): HandleAddToPlaylistTracksUiUpdate

    @Binds
    @AddToPlaylistScope
    fun bindAddToPlaylistCommunication(obj: AddToPlaylistCommunication.Base): AddToPlaylistCommunication

    @Binds
    @AddToPlaylistScope
    fun bindAddToPlaylistUiStateCommunication(obj: AddToPlaylistUiStateCommunication.Base): AddToPlaylistUiStateCommunication

    @Binds
    @AddToPlaylistScope
    fun bindACachedTracksCommunication(obj: CachedTracksCommunication.Base): CachedTracksCommunication

//    @Binds
//    @AddToPlaylistScope
//    fun bindAddToPlaylistSelectBtnCommunication(obj: PlaylistDataCommunicationCommunication.Base): PlaylistDataCommunicationCommunication



//    @Binds
//    @AddToPlaylistScope
//    fun bindHandleFavoritesListFromCacheSelected(obj: HandleFavoritesListFromCacheSelected.Base): HandleFavoritesListFromCacheSelected

    @Binds
    @[IntoMap ViewModelKey(AddToPlaylistViewModel::class)]
    fun bindFavoritesViewModel(addToPlaylistViewModel: AddToPlaylistViewModel): ViewModel
}