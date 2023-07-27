package com.example.musicapp.creteplaylist.di

import androidx.lifecycle.ViewModel
import com.example.musicapp.app.core.CreatePlaylistInteractor
import com.example.musicapp.app.core.EditPlaylistInteractor
import com.example.musicapp.app.core.PlaylistDataInteractor
import com.example.musicapp.creteplaylist.presentation.CreatePlaylistViewModel
import com.example.musicapp.creteplaylist.presentation.PlaylistDataResultMapper
import com.example.musicapp.creteplaylist.presentation.PlaylistSaveBtnUiStateCommunication
import com.example.musicapp.creteplaylist.presentation.SelectedTracksCommunication
import com.example.musicapp.editplaylist.presentation.EditPlaylistViewModel
import com.example.musicapp.editplaylist.presentation.PlaylistResultEditPlaylistUpdateMapper
import com.example.musicapp.editplaylist.presentation.TitleStateCommunication
import com.example.musicapp.main.di.ViewModelKey
import com.example.musicapp.userplaylists.di.PlaylistsScope
import com.example.musicapp.userplaylists.presentation.PlaylistUi
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap

/**
 * Created by HP on 16.07.2023.
 **/
@Module
interface PlaylistDataModule {

    @Binds
    @PlaylistDataScope
    fun bindPlaylistUiToIdMapper(obj: PlaylistUi.ToIdMapper): PlaylistUi.Mapper<Int>

    @Binds
    @PlaylistDataScope
    fun bindTitleStateCommunication(obj: TitleStateCommunication.Base): TitleStateCommunication

    @Binds
    @PlaylistDataScope
    fun bindPlaylistSaveBtnUiStateCommunication(obj: PlaylistSaveBtnUiStateCommunication.Base): PlaylistSaveBtnUiStateCommunication

    @Binds
    @PlaylistDataScope
    fun bindPlaylistResultEditPlaylistUpdateMapper(obj: PlaylistResultEditPlaylistUpdateMapper.Base): PlaylistResultEditPlaylistUpdateMapper

    @Binds
    @PlaylistDataScope
    fun bindCreatePlaylistInteractor(obj: PlaylistDataInteractor): CreatePlaylistInteractor

    @Binds
    @PlaylistDataScope
    fun bindEditPlaylistInteractor(obj: PlaylistDataInteractor): EditPlaylistInteractor


    @Binds
    @PlaylistDataScope
    fun bindPlaylistDataResultMapper(obj: PlaylistDataResultMapper.Base): PlaylistDataResultMapper

    @Binds
    @[IntoMap ViewModelKey(EditPlaylistViewModel::class)]
    fun bindEditPlaylistViewModel(editPlaylistViewModel: EditPlaylistViewModel): ViewModel

    @Binds
    @[IntoMap ViewModelKey(CreatePlaylistViewModel::class)]
    fun bindFavoritesViewModel(createPlaylistViewModel: CreatePlaylistViewModel): ViewModel
}

@Module
class PlaylistDataModuleProvides {

    @Provides
    @PlaylistDataScope
    fun provideSelectedTracksCommunication(): SelectedTracksCommunication {
        return SelectedTracksCommunication.Base
    }
}