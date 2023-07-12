package com.example.musicapp.favorites.di

import com.example.musicapp.favorites.presentation.DeleteDialogFragment
import com.example.musicapp.favorites.presentation.FavoritesFragment
import com.example.musicapp.trending.di.TrendingComponent
import com.example.musicapp.trending.di.TrendingModule
import com.example.musicapp.trending.di.TrendingScope
import com.example.musicapp.trending.presentation.TrendingFragment
import dagger.Subcomponent

/**
 * Created by HP on 21.03.2023.
 **/
@FavoritesScope
@Subcomponent(modules = [FavoritesModule::class])
interface FavoriteComponent {

    @Subcomponent.Builder
    interface Builder{
        fun build(): FavoriteComponent
    }

    fun inject(favoritesFragment: FavoritesFragment)
    fun inject(deleteDialogFragment: DeleteDialogFragment)



}