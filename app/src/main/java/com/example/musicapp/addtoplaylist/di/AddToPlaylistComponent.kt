package com.example.musicapp.addtoplaylist.di

import com.example.musicapp.addtoplaylist.presentation.AddToPlaylistFragment
import com.example.musicapp.creteplaylist.presentation.CreatePlaylistFragment
import com.example.musicapp.favorites.presentation.DeleteDialogFragment
import com.example.musicapp.favorites.presentation.FavoritesTracksFragment
import com.example.musicapp.trending.di.TrendingComponent
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