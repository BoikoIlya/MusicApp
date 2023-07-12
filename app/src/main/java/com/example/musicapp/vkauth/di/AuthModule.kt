package com.example.musicapp.vkauth.di

import androidx.lifecycle.ViewModel

import com.example.musicapp.main.di.ViewModelKey

import com.example.musicapp.vkauth.presentation.AuthCommunication
import com.example.musicapp.vkauth.presentation.AuthViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

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

