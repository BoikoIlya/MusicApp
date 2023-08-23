package com.kamancho.melisma.selectplaylist.di


import com.kamancho.melisma.selectplaylist.presentation.SelectPlaylistFragment
import dagger.Subcomponent

/**
 * Created by HP on 01.05.2023.
 **/
@SelectPlaylistScope
@Subcomponent(modules = [SelectPlaylistModule::class,SelectPlaylistModuleProvides::class])
interface SelectPlaylistComponent {

    @Subcomponent.Builder
    interface Builder{
        fun build(): SelectPlaylistComponent
    }

    fun inject(selectPlaylistFragment: SelectPlaylistFragment)
}