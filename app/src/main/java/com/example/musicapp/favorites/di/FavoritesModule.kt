package com.example.musicapp.favorites.di

import androidx.lifecycle.ViewModel
import com.example.musicapp.favorites.presentation.*
import com.example.musicapp.main.di.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap

/**
 * Created by HP on 21.03.2023.
 **/
@Module
interface FavoritesModule {

    @FavoritesScope
    @Binds
    fun bindHandlerFavoritesTracksUiUpdate(obj: HandlerFavoritesTracksUiUpdate.Base): HandlerFavoritesTracksUiUpdate

    @FavoritesScope
    @Binds
    fun bindFavoritesCommunication(obj: FavoritesCommunication.Base): FavoritesCommunication

    @FavoritesScope
    @Binds
    fun bindFavoritesStateCommunication(obj: FavoritesStateCommunication.Base): FavoritesStateCommunication

    @FavoritesScope
    @Binds
    fun bindFavoritesTrackListCommunication(obj: FavoritesTrackListCommunication.Base): FavoritesTrackListCommunication

    @FavoritesScope
    @Binds
    fun bindTracksResultToFavoriteTracksCommunicationMapper(obj: TracksResultToFavoriteTracksCommunicationMapper.Base): TracksResultToFavoriteTracksCommunicationMapper



    @Binds
    @[IntoMap ViewModelKey(FavoritesViewModel::class)]
    fun bindFavoritesViewModel(favoritesViewModel: FavoritesViewModel): ViewModel

    @Binds
    @[IntoMap ViewModelKey(DeleteDialogViewModel::class)]
    fun bindDeleteDialogViewModel(deleteDialogViewModel: DeleteDialogViewModel): ViewModel
}
