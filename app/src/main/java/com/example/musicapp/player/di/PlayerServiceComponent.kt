package com.example.musicapp.player.di

import com.example.musicapp.player.presentation.PlayerService
import dagger.Subcomponent

/**
 * Created by HP on 30.01.2023.
 **/
@PlayerServiceScope
@Subcomponent(modules = [PlayerModule::class])
interface PlayerServiceComponent {

    @Subcomponent.Builder
    interface Builder{
        fun build(): PlayerServiceComponent
    }

    fun inject(playerService: PlayerService)
}