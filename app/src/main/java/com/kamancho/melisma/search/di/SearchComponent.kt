package com.kamancho.melisma.search.di


import com.kamancho.melisma.search.presentation.BaseSearchPlaylistsFragment
import com.kamancho.melisma.search.presentation.BaseSearchTracksFragment
import com.kamancho.melisma.search.presentation.SearchFragment
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