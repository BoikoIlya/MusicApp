package com.example.musicapp.userplaylists.di

import com.example.musicapp.favoritesplaylistdetails.di.FavoritesPlaylistDetailsComponent
import com.example.musicapp.userplaylists.presentation.DeletePlaylistDialogFragment
import com.example.musicapp.userplaylists.presentation.FavoritesPlaylistsFragment
import com.example.musicapp.userplaylists.presentation.PlaylistsMenuDialogBottomSheetFragment
import dagger.Subcomponent

/**
 * Created by HP on 28.01.2023.
 **/
@PlaylistsScope
@Subcomponent(modules = [PlaylistsModule::class,PlaylistsModuleProvides::class])
interface PlaylistsComponent {

    @Subcomponent.Builder
    interface Builder{
       fun build(): PlaylistsComponent
    }

    fun inject(playlistsFragment: FavoritesPlaylistsFragment)

    fun inject(playlistsMenuBottomSheet: PlaylistsMenuDialogBottomSheetFragment)

    fun inject(deleteDialogFragment: DeletePlaylistDialogFragment)

    fun playlistDetailsComponent(): FavoritesPlaylistDetailsComponent.Builder
}