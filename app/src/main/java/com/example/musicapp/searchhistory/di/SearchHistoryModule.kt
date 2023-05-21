package com.example.musicapp.searchhistory.di

import androidx.lifecycle.ViewModel
import androidx.media3.common.MediaItem
import com.example.musicapp.app.SpotifyDto.SearchTracks
import com.example.musicapp.main.di.ViewModelKey
import com.example.musicapp.searchhistory.presentation.ToHistoryItemMapper
import com.example.musicapp.searchhistory.data.HistoryDeleteResult
import com.example.musicapp.searchhistory.data.SearchHistoryRepository
import com.example.musicapp.searchhistory.presentation.HistoryDeleteResultMapper
import com.example.musicapp.searchhistory.presentation.SearchHistoryCommunication
import com.example.musicapp.searchhistory.presentation.SearchHistoryViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

/**
 * Created by HP on 01.05.2023.
 **/
@Module
interface SearchHistoryModule {



    @Binds
    @SearchHistoryScope
    fun bindHistoryDeleteResultMapper( obj: HistoryDeleteResultMapper): HistoryDeleteResult.Mapper<Unit>

    @Binds
    @SearchHistoryScope
    fun bindSearchHistoryRepository(obj: SearchHistoryRepository.Base): SearchHistoryRepository

    @Binds
    @SearchHistoryScope
    fun bindSearchHistoryCommunication(obj: SearchHistoryCommunication.Base): SearchHistoryCommunication

    @Binds
    @SearchHistoryScope
    fun bindToHistoryItemMapper(obj: ToHistoryItemMapper.Base): ToHistoryItemMapper


    @Binds
    @[IntoMap ViewModelKey(SearchHistoryViewModel::class)]
    fun bindSearchHistoryViewModel(searchHistoryViewModel: SearchHistoryViewModel): ViewModel

}