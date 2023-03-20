package com.example.musicapp.main.di

import android.content.Context
import androidx.media3.common.util.UnstableApi
import com.example.musicapp.main.presentation.MainActivity
import com.example.musicapp.player.di.PlayerComponent
import com.example.musicapp.trending.di.TrendingComponent
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@UnstableApi /**
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

    fun inject(mainActivity: MainActivity)

    fun trendingComponent(): TrendingComponent.Builder

    fun playerComponent(): PlayerComponent.Builder



}