package com.example.musicapp.favoritesplaylistdetails.di

import com.example.musicapp.deletetrackfromplaylist.di.DeleteTrackFromPlaylistComponent
import com.example.musicapp.favoritesplaylistdetails.presentation.FavoritesPlaylistDetailsFragment
import dagger.Subcomponent

/**
 * Created by HP on 28.01.2023.
 **/
@FavoritesPlaylistDetailsScope
@Subcomponent(modules = [FavoritesPlaylistDetailsModule::class,FavoritesPlaylistDetailsProvidesModule::class])
interface FavoritesPlaylistDetailsComponent {

    @Subcomponent.Builder
    interface Builder{
       fun build(): FavoritesPlaylistDetailsComponent
    }

    fun inject(playlistFragment: FavoritesPlaylistDetailsFragment)

    fun deleteTrackFromPlaylistComponent(): DeleteTrackFromPlaylistComponent.Builder
}