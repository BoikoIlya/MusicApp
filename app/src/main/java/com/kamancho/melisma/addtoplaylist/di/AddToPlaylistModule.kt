package com.kamancho.melisma.addtoplaylist.di

import androidx.lifecycle.ViewModel
import com.kamancho.melisma.addtoplaylist.presentation.AddToPlaylistCommunication
import com.kamancho.melisma.addtoplaylist.presentation.AddToPlaylistUiStateCommunication
import com.kamancho.melisma.addtoplaylist.presentation.AddToPlaylistViewModel
import com.kamancho.melisma.addtoplaylist.presentation.CachedTracksCommunication
import com.kamancho.melisma.addtoplaylist.presentation.HandleAddToPlaylistTracksUiUpdate
import com.kamancho.melisma.addtoplaylist.presentation.HandleCachedTracksSelected

import com.kamancho.melisma.favorites.presentation.FavoritesTracksLoadingCommunication
import com.kamancho.melisma.main.di.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

/**
 * Created by HP on 16.07.2023.
 **/
@Module
interface AddToPlaylistModule {


    @Binds
    @AddToPlaylistScope
    fun bindFavoritesTracksLoadingCommunication(obj: FavoritesTracksLoadingCommunication.Base): FavoritesTracksLoadingCommunication

    @Binds
    @AddToPlaylistScope
    fun bindHandleCachedTracksSelected(obj: HandleCachedTracksSelected.Base): HandleCachedTracksSelected

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


    @Binds
    @[IntoMap ViewModelKey(AddToPlaylistViewModel::class)]
    fun bindFavoritesViewModel(addToPlaylistViewModel: AddToPlaylistViewModel): ViewModel
}