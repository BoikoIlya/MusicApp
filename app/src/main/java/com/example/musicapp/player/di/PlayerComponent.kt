package com.example.musicapp.player.di

import com.example.musicapp.player.presentation.PlayerFragment
import com.example.musicapp.player.presentation.PlayerService
import com.example.musicapp.queue.presenatation.QueueFragment
import dagger.Subcomponent

/**
 * Created by HP on 30.01.2023.
 **/
@PlayerServiceScope
@Subcomponent(modules = [PlayerModule::class, BindsPlayerModule::class])
interface PlayerComponent {

    @Subcomponent.Builder
    interface Builder{
        fun build(): PlayerComponent
    }

    fun inject(playerFragment: PlayerFragment)
    fun inject(playerService: PlayerService)
}