package com.example.musicapp.trending.di

import android.content.Context
import com.example.musicapp.trending.data.TrendingRepository
import com.example.musicapp.trending.data.cloud.TrendingService
import dagger.Binds
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

/**
 * Created by HP on 28.01.2023.
 **/
@Module
class TrendingModuleNetworkAndCache(private val context: Context){

    companion object{
        private const val baseUrl = "https://api.napster.com"
    }

    @Provides
    @Singleton
    fun provideTrendingService(): TrendingService{
        return Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(TrendingService::class.java)
    }

    @

}

@Module
interface TrendingArchitectureModule{

    @Binds
    @Singleton
    fun bindRepository(obj: TrendingRepository.Base): TrendingRepository



}

