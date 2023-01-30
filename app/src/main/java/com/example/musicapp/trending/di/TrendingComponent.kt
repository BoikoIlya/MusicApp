package com.example.musicapp.trending.di

import com.example.musicapp.app.app.di.AppComponent
import com.example.musicapp.trending.presentation.TrendingFragment
import dagger.Component
import dagger.Subcomponent
import javax.inject.Singleton

/**
 * Created by HP on 28.01.2023.
 **/
@TrendingScope
@Subcomponent(modules = [TrendingModule::class])
interface TrendingComponent {

    @Subcomponent.Builder
    interface Builder{
       fun build(): TrendingComponent
    }

    fun inject(trendingFragment: TrendingFragment)

}