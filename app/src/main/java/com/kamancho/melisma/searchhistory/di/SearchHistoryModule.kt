package com.kamancho.melisma.searchhistory.di

import androidx.lifecycle.ViewModel
import com.kamancho.melisma.app.core.MusicDatabase
import com.kamancho.melisma.main.di.ViewModelKey
import com.kamancho.melisma.searchhistory.presentation.ToHistoryItemMapper
import com.kamancho.melisma.searchhistory.data.HistoryDeleteResult
import com.kamancho.melisma.searchhistory.data.SearchHistoryRepository
import com.kamancho.melisma.searchhistory.data.cache.HistoryDao
import com.kamancho.melisma.searchhistory.presentation.BaseSearchHistoryPlaylistsListViewModel
import com.kamancho.melisma.searchhistory.presentation.BaseSearchHistoryTracksListViewModel
import com.kamancho.melisma.searchhistory.presentation.ClearSearchHistoryDialogViewModel
import com.kamancho.melisma.searchhistory.presentation.HistoryDeleteResultMapper
import com.kamancho.melisma.searchhistory.presentation.SearchHistoryCommunication
import com.kamancho.melisma.searchhistory.presentation.SearchHistorySingleStateCommunication
import com.kamancho.melisma.searchhistory.presentation.SearchHistoryInputStateCommunication
import com.kamancho.melisma.searchhistory.presentation.SearchHistoryViewModel
import dagger.Binds
import dagger.Module
import dagger.Provides
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