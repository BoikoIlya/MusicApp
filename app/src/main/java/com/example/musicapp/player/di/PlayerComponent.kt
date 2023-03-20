package com.example.musicapp.player.di

import com.example.musicapp.player.presentation.PlayerActivity
import com.example.musicapp.player.presentation.PlayerService
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

    fun inject(playerActivity: PlayerActivity)
    fun inject(playerService: PlayerService)
}