package com.example.musicapp.searchhistory.di


import com.example.musicapp.search.di.SearchScope
import com.example.musicapp.search.presentation.SearchFragment
import com.example.musicapp.searchhistory.presentation.SearchHistoryFragment
import com.example.musicapp.searchhistory.presentation.SearchHistoryViewModel
import dagger.Subcomponent

/**
 * Created by HP on 01.05.2023.
 **/
@SearchHistoryScope
@Subcomponent(modules = [SearchHistoryModule::class])
interface SearchHistoryComponent {

    @Subcomponent.Builder
    interface Builder{
        fun build(): SearchHistoryComponent
    }

    fun inject(searchHistoryFragment: SearchHistoryFragment)
}