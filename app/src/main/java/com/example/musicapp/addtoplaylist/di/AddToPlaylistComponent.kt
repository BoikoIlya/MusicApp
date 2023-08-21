package com.example.musicapp.addtoplaylist.di

import com.example.musicapp.addtoplaylist.presentation.AddToPlaylistFragment
import dagger.Subcomponent

/**
 * Created by HP on 21.03.2023.
 **/
@AddToPlaylistScope
@Subcomponent(modules = [AddToPlaylistModule::class])
interface AddToPlaylistComponent {

    @Subcomponent.Builder
    interface Builder{
        fun build(): AddToPlaylistComponent
    }

    fun inject(addToPlaylistFragment: AddToPlaylistFragment)

}