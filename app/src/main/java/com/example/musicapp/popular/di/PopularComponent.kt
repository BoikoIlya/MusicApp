package com.example.musicapp.popular.di


import com.example.musicapp.popular.presentation.PopularFragment
import com.example.musicapp.search.presentation.BaseSearchPlaylistsFragment
import com.example.musicapp.search.presentation.BaseSearchTracksFragment
import com.example.musicapp.search.presentation.SearchFragment
import dagger.Subcomponent

/**
 * Created by HP on 01.05.2023.
 **/
@PopularScope
@Subcomponent(modules = [PopularModule::class,PopularModuleProvides::class])
interface PopularComponent {

    @Subcomponent.Builder
    interface Builder{
        fun build(): PopularComponent
    }

    fun inject(popularFragment: PopularFragment)

}