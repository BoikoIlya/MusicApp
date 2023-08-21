package com.example.musicapp.friends.di

import com.example.musicapp.favorites.presentation.DeleteTrackDialogFragment
import com.example.musicapp.favorites.presentation.FavoritesBottomSheetMenuFragment
import com.example.musicapp.favorites.presentation.FavoritesTracksFragment
import com.example.musicapp.friends.presentation.FriendsFragment
import dagger.Subcomponent

/**
 * Created by HP on 21.03.2023.
 **/
@FriendsScope
@Subcomponent(modules = [FriendsModule::class,FriendsProvidesModule::class])
interface FriendsComponent {

    @Subcomponent.Builder
    interface Builder{
        fun build(): FriendsComponent
    }

    fun inject(friendsFragment: FriendsFragment)


}