package com.example.musicapp.search.di

import androidx.lifecycle.ViewModel
import androidx.media3.common.MediaItem
import com.example.musicapp.app.SpotifyDto.SearchTracks
import com.example.musicapp.main.di.ViewModelKey
import com.example.musicapp.queue.presenatation.QueueViewModel
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

//    @Binds
//    @SearchScope
//    fun bindSearchTracksToMediaItemsMapper(obj: SearchTracks.SearchTracksToMediaItemMapper): SearchTracks.Mapper<List<MediaItem>>

    @Binds
    @SearchScope
    fun bindSearchRepository(obj: SearchRepository.Base): SearchRepository

    @Binds
    @[IntoMap ViewModelKey(SearchViewModel::class)]
    fun bindSearchViewModel(searchViewModel: SearchViewModel): ViewModel

}