package com.kamancho.melisma.searchhistory.di


import com.kamancho.melisma.searchhistory.presentation.ClearSearchHistoryDialogFragment
import com.kamancho.melisma.searchhistory.presentation.SearchHistoryFragment
import com.kamancho.melisma.searchhistory.presentation.SearchHistoryListFragment
import dagger.Subcomponent

/**
 * Created by HP on 01.05.2023.
 **/
@SearchHistoryScope
@Subcomponent(modules = [SearchHistoryModule::class,ProvidesSearchHistoryModule::class])
interface SearchHistoryComponent {

    @Subcomponent.Builder
    interface Builder{
        fun build(): SearchHistoryComponent
    }

    fun inject(searchHistoryFragment: SearchHistoryFragment)
    fun inject(clearSearchHistoryDialogFragment: ClearSearchHistoryDialogFragment)
    fun inject(baseSearchHistoryTracksListFragment: SearchHistoryListFragment.BaseSearchHistoryTracksListFragment)
    fun inject(baseSearchHistoryPlaylistsListFragment: SearchHistoryListFragment.BaseSearchHistoryPlaylistsListFragment)
}