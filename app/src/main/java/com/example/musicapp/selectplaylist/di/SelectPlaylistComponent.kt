package com.example.musicapp.selectplaylist.di


import com.example.musicapp.search.presentation.SearchFragment
import com.example.musicapp.selectplaylist.presentation.SelectPlaylistFragment
import dagger.Subcomponent

/**
 * Created by HP on 01.05.2023.
 **/
@SelectPlaylistScope
@Subcomponent(modules = [SelectPlaylistModule::class])
interface SelectPlaylistComponent {

    @Subcomponent.Builder
    interface Builder{
        fun build(): SelectPlaylistComponent
    }

    fun inject(selectPlaylistFragment: SelectPlaylistFragment)
}