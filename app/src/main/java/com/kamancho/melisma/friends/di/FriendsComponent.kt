package com.kamancho.melisma.friends.di

import com.kamancho.melisma.friends.presentation.FriendsFragment
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