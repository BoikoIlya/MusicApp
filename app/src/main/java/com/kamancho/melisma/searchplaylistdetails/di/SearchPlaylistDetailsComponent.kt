package com.kamancho.melisma.searchplaylistdetails.di

import com.kamancho.melisma.searchplaylistdetails.presentation.SearchPlaylistDetailsFragment
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