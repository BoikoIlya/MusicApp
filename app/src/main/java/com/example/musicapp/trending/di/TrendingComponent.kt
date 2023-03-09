package com.example.musicapp.trending.di

import com.example.musicapp.trending.presentation.TrendingFragment
import dagger.Subcomponent

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