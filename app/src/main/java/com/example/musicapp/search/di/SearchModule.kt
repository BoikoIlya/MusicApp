package com.example.musicapp.search.di

import androidx.lifecycle.ViewModel
import androidx.media3.common.MediaItem
import androidx.paging.PagingSource
import com.example.musicapp.app.core.HandleResponse
import com.example.musicapp.app.vkdto.Item
import com.example.musicapp.main.di.ViewModelKey
import com.example.musicapp.search.data.SearchRepository
import com.example.musicapp.search.presentation.SearchViewModel
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap

/**
 * Created by HP on 01.05.2023.
 **/
@Module
interface SearchModule {


    @Binds
    @SearchScope
    fun bindSearchRepository(obj: SearchRepository.Base): SearchRepository

    @Binds
    @SearchScope
    fun bindCloudTrackToMediaItemMapper(obj: Item.Mapper.CloudTrackToMediaItemMapper): Item.Mapper<MediaItem>



    @Binds
    @[IntoMap ViewModelKey(SearchViewModel::class)]
    fun bindSearchViewModel(searchViewModel: SearchViewModel): ViewModel

}
