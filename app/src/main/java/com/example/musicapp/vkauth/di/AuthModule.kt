package com.example.musicapp.vkauth.di

import androidx.lifecycle.ViewModel
import androidx.media3.common.MediaItem
import com.example.musicapp.app.SpotifyDto.Item
import com.example.musicapp.app.core.ImageLoader
import com.example.musicapp.app.core.HandleError
import com.example.musicapp.main.di.ViewModelKey
import com.example.musicapp.trending.data.TrendingRepository
import com.example.musicapp.trending.domain.PlaylistDomain
import com.example.musicapp.trending.domain.TrackDomain
import com.example.musicapp.trending.domain.TrendingInteractor
import com.example.musicapp.trending.presentation.*
import com.example.musicapp.vkauth.presentation.AuthCommunication
import com.example.musicapp.vkauth.presentation.AuthViewModel
import com.example.testapp.spotifyDto.Track
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

