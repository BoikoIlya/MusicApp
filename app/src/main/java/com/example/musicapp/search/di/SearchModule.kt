package com.example.musicapp.search.di

import androidx.lifecycle.ViewModel
import androidx.media3.common.MediaItem
import com.example.musicapp.app.vkdto.TrackItem
import com.example.musicapp.main.di.ViewModelKey
import com.example.musicapp.search.data.SearchRepository
import com.example.musicapp.search.presentation.SearchViewModel
import dagger.Binds
import dagger.Module
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
    fun bindCloudTrackToMediaItemMapper(obj: TrackItem.Mapper.CloudTrackToMediaItemMapper): TrackItem.Mapper<MediaItem>



    @Binds
    @[IntoMap ViewModelKey(SearchViewModel::class)]
    fun bindSearchViewModel(searchViewModel: SearchViewModel): ViewModel

}
