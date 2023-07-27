package com.example.musicapp.playlist.di

import com.example.musicapp.playlist.presentation.PlaylistFragment
import dagger.Subcomponent

/**
 * Created by HP on 28.01.2023.
 **/
@PlaylistScope
@Subcomponent(modules = [PlaylistModule::class])
interface PlaylistComponent {

    @Subcomponent.Builder
    interface Builder{
       fun build(): PlaylistComponent
    }

    fun inject(playlistFragment: PlaylistFragment)


}