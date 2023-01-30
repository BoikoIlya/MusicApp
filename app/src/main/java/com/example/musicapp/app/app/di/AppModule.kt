package com.example.musicapp.app.app.di

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.musicapp.app.DispatchersList
import com.example.musicapp.app.ManagerResource
import com.example.musicapp.trending.data.cloud.TrendingService
import com.example.musicapp.trending.presentation.TrendingViewModel
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

/**
 * Created by HP on 29.01.2023.
 **/
@Module
class AppModule {

    companion object{
        private const val baseUrl = "https://api.napster.com"
    }

    @Provides
    @Singleton
    fun provideTrendingService(): TrendingService {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(TrendingService::class.java)
    }

    @Provides
    @Singleton
    fun provideManagerResource(context: Context): ManagerResource{
        return ManagerResource.Base(context)
    }

}

@Module
interface AppBindModule{

    @Singleton
    @Binds
    fun bindDispatcherList(obj: DispatchersList.Base): DispatchersList

    @Binds
     fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory


}