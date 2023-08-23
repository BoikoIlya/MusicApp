package com.kamancho.melisma.creteplaylist.di

import com.kamancho.melisma.addtoplaylist.di.AddToPlaylistComponent
import com.kamancho.melisma.creteplaylist.presentation.CreatePlaylistFragment
import com.kamancho.melisma.editplaylist.presentation.EditPlaylistFragment
import com.kamancho.melisma.searchplaylistdetails.di.SearchPlaylistDetailsComponent
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