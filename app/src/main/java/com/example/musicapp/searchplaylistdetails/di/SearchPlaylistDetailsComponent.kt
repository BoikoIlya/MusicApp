package com.example.musicapp.searchplaylistdetails.di

import com.example.musicapp.deletetrackfromplaylist.di.DeleteTrackFromPlaylistComponent
import com.example.musicapp.favoritesplaylistdetails.presentation.FavoritesPlaylistDetailsFragment
import com.example.musicapp.searchplaylistdetails.presentation.SearchPlaylistDetailsFragment
import dagger.Subcomponent

/**
 * Created by HP on 28.01.2023.
 **/
@SearchPlaylistDetailsScope
@Subcomponent(modules = [SearchPlaylistDetailsModule::class,SearchPlaylistDetailsProvidesModule::class])
interface SearchPlaylistDetailsComponent {

    @Subcomponent.Builder
    interface Builder{
       fun build(): SearchPlaylistDetailsComponent
    }

    fun inject(searchPlaylistDetailsFragment: SearchPlaylistDetailsFragment)

}