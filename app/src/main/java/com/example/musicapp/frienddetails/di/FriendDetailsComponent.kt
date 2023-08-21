package com.example.musicapp.frienddetails.di

import com.example.musicapp.frienddetails.presentation.FriendDetailsFragment
import com.example.musicapp.frienddetails.presentation.FriendPlaylistsFragment
import com.example.musicapp.frienddetails.presentation.FriendTracksFragment
import dagger.Subcomponent

/**
 * Created by HP on 28.01.2023.
 **/
@FriendDetailsScope
@Subcomponent(modules = [FriendDetailsModule::class,FriendDetailsProvidesModule::class])
interface FriendDetailsComponent {

    @Subcomponent.Builder
    interface Builder{
       fun build(): FriendDetailsComponent
    }

    fun inject(friendTracksFragment: FriendTracksFragment)
    fun inject(friendPlaylistsFragement: FriendPlaylistsFragment)
    fun inject(friendDetailsFragment: FriendDetailsFragment)

}