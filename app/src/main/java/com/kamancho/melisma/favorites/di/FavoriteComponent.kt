package com.kamancho.melisma.favorites.di

import com.kamancho.melisma.favorites.presentation.DeleteTrackDialogFragment
import com.kamancho.melisma.favorites.presentation.FavoritesBottomSheetMenuFragment
import com.kamancho.melisma.favorites.presentation.FavoritesTracksFragment
import dagger.Subcomponent

/**
 * Created by HP on 21.03.2023.
 **/
@FavoritesScope
@Subcomponent(modules = [FavoritesModule::class,FavoritesProvidesModule::class])
interface FavoriteComponent {

    @Subcomponent.Builder
    interface Builder{
        fun build(): FavoriteComponent
    }

    fun inject(favoritesFragment: FavoritesTracksFragment)
    fun inject(deleteDialogFragment: DeleteTrackDialogFragment)
    fun inject(favoritesBottomSheetMenuFragment: FavoritesBottomSheetMenuFragment)

}