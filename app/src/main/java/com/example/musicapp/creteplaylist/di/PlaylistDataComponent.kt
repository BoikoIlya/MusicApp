package com.example.musicapp.creteplaylist.di

import com.example.musicapp.addtoplaylist.di.AddToPlaylistComponent
import com.example.musicapp.creteplaylist.presentation.CreatePlaylistFragment
import com.example.musicapp.editplaylist.presentation.EditPlaylistFragment
import com.example.musicapp.searchplaylistdetails.di.SearchPlaylistDetailsComponent
import dagger.Subcomponent

/**
 * Created by HP on 21.03.2023.
 **/
@PlaylistDataScope
@Subcomponent(modules = [PlaylistDataModule::class, PlaylistDataModuleProvides::class])
interface PlaylistDataComponent {

    @Subcomponent.Builder
    interface Builder{
        fun build(): PlaylistDataComponent
    }

    fun inject(createPlaylistFragment: CreatePlaylistFragment)

    fun inject(editPlaylistFragment: EditPlaylistFragment)

    fun addToPlaylistComponent(): AddToPlaylistComponent.Builder

    fun searchPlaylistDetailsComponent(): SearchPlaylistDetailsComponent.Builder
}