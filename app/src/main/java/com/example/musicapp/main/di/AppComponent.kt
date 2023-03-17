package com.example.musicapp.main.di

import android.content.Context
import com.example.musicapp.main.presentation.MainActivity
import com.example.musicapp.player.di.PlayerServiceComponent
import com.example.musicapp.trending.di.TrendingComponent
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

/**
 * Created by HP on 29.01.2023.
 **/
@Singleton
@Component(modules = [AppModule::class, AppBindModule::class])
interface AppComponent {

    @Component.Builder
    interface Builder{

        @BindsInstance
        fun context(context: Context): Builder

        fun build(): AppComponent
    }

    fun inject(mainActivity: _root_ide_package_.com.example.musicapp.main.presentation.MainActivity)

    fun trendingComponent(): TrendingComponent.Builder

    fun playerServiceComponent(): PlayerServiceComponent.Builder



}