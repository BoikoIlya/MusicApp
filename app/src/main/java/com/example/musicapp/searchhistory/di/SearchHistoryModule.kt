package com.example.musicapp.searchhistory.di

import androidx.lifecycle.ViewModel
import com.example.musicapp.app.core.MusicDatabase
import com.example.musicapp.main.di.ViewModelKey
import com.example.musicapp.searchhistory.presentation.ToHistoryItemMapper
import com.example.musicapp.searchhistory.data.HistoryDeleteResult
import com.example.musicapp.searchhistory.data.SearchHistoryRepository
import com.example.musicapp.searchhistory.data.cache.HistoryDao
import com.example.musicapp.searchhistory.presentation.BaseSearchHistoryPlaylistsListViewModel
import com.example.musicapp.searchhistory.presentation.BaseSearchHistoryTracksListViewModel
import com.example.musicapp.searchhistory.presentation.ClearSearchHistoryDialogViewModel
import com.example.musicapp.searchhistory.presentation.HistoryDeleteResultMapper
import com.example.musicapp.searchhistory.presentation.SearchHistoryCommunication
import com.example.musicapp.searchhistory.presentation.SearchHistorySingleStateCommunication
import com.example.musicapp.searchhistory.presentation.SearchHistoryInputStateCommunication
import com.example.musicapp.searchhistory.presentation.SearchHistoryListViewModel
import com.example.musicapp.searchhistory.presentation.SearchHistoryViewModel
import com.example.musicapp.searchhistory.presentation.SearchQueryCommunication
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.Reusable
import dagger.multibindings.IntoMap
import javax.inject.Singleton

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
    fun bindSearchHistoryNavigationCommunication( obj: SearchHistoryInputStateCommunication.Base): SearchHistoryInputStateCommunication

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

    @Binds
    @[IntoMap ViewModelKey(ClearSearchHistoryDialogViewModel::class)]
    fun bindClearSearchHistoryDialogViewModel(clearSearchHistoryDialogViewModel: ClearSearchHistoryDialogViewModel): ViewModel

    @Binds
    @[IntoMap ViewModelKey(BaseSearchHistoryPlaylistsListViewModel::class)]
    fun bindBaseSearchHistoryPlaylistsListViewModel(baseSearchHistoryPlaylistsListViewModel: BaseSearchHistoryPlaylistsListViewModel): ViewModel    @Binds

    @[IntoMap ViewModelKey(BaseSearchHistoryTracksListViewModel::class)]
    fun bindBaseSearchHistoryTracksListViewModel(baseSearchHistoryPlaylistsListViewModel: BaseSearchHistoryTracksListViewModel): ViewModel


}

@Module
class ProvidesSearchHistoryModule{

    @Provides
    @SearchHistoryScope
    fun provideHistoryDao(db: MusicDatabase): HistoryDao {
        return db.getHistoryDao()
    }

    @Provides
    @SearchHistoryScope
    fun provideSearchHistorySingleStateCommunication(): SearchHistorySingleStateCommunication{
        return SearchHistorySingleStateCommunication.Base
    }


}