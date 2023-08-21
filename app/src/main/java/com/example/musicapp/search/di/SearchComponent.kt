package com.example.musicapp.search.di


import com.example.musicapp.search.presentation.BaseSearchPlaylistsFragment
import com.example.musicapp.search.presentation.BaseSearchTracksFragment
import com.example.musicapp.search.presentation.SearchFragment
import dagger.Subcomponent

/**
 * Created by HP on 01.05.2023.
 **/
@SearchScope
@Subcomponent(modules = [SearchModule::class,SearchModuleProvides::class])
interface SearchComponent {

    @Subcomponent.Builder
    interface Builder{
        fun build(): SearchComponent
    }

    fun inject(baseSearchPlaylistsFragment: BaseSearchPlaylistsFragment)

    fun inject(baseSearchTracksFragment: BaseSearchTracksFragment)

    fun inject(searchFragment: SearchFragment)
}