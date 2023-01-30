package com.example.musicapp.app.app.di

import android.app.Application
import android.content.Context
import com.example.musicapp.trending.di.TrendingComponent
import com.example.musicapp.trending.presentation.TrendingFragment
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

/**
 * Created by HP on 29.01.2023.
 **/
@Singleton
@Component(modules = [AppModule::class,AppBindModule::class])
interface AppComponent {

    @Component.Builder
    interface Builder{

        @BindsInstance
        fun context(context: Context): Builder

        fun build(): AppComponent
    }

    fun trendingComponent(): TrendingComponent.Builder


}