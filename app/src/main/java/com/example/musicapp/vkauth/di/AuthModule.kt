package com.example.musicapp.vkauth.di

import androidx.lifecycle.ViewModel
import com.example.musicapp.app.core.ExtendedGsonConverterFactory
import com.example.musicapp.main.data.cloud.AuthorizationService
import com.example.musicapp.main.di.AppModule

import com.example.musicapp.main.di.ViewModelKey

import com.example.musicapp.vkauth.presentation.AuthCommunication
import com.example.musicapp.vkauth.presentation.AuthViewModel
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import javax.inject.Singleton

/**
 * Created by HP on 28.01.2023.
 **/


@Module
interface AuthModule{

    @Binds
    @AuthScope
    fun bindAuthCommunication(obj: AuthCommunication.Base): AuthCommunication

    @Binds
    @[IntoMap ViewModelKey(AuthViewModel::class)]
    fun bindTrendingViewModel(authViewModel: AuthViewModel): ViewModel

}



