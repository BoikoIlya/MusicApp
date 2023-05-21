package com.example.musicapp.search.presentation

import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.musicapp.app.core.BaseViewModel
import com.example.musicapp.app.core.DispatchersList
import com.example.musicapp.app.core.TracksRepository
import com.example.musicapp.favorites.presentation.TracksResultToUiEventCommunicationMapper
import com.example.musicapp.main.data.TemporaryTracksCache
import com.example.musicapp.main.presentation.PlayerCommunication
import com.example.musicapp.main.presentation.PlayerCommunicationState
import com.example.musicapp.search.data.SearchPagingSource
import com.example.musicapp.search.data.SearchRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by HP on 01.05.2023.
 **/
@ExperimentalCoroutinesApi
@FlowPreview
class SearchViewModel @Inject constructor(
    private val searchRepository: SearchRepository,
    playerCommunication: PlayerCommunication,
    private val dispatchersList: DispatchersList,
    temporaryTracksCache: TemporaryTracksCache,
    private val tracksRepository: TracksRepository,
    private val mapper: TracksResultToUiEventCommunicationMapper,
): BaseViewModel(playerCommunication, temporaryTracksCache, dispatchersList){

    private var errorMessage = ""

    private val searchTerm = MutableStateFlow("")

    private var searchTermStr = ""

    val tracks: Flow<PagingData<MediaItem>> = searchTerm
        .flatMapLatest {
            if(searchTermStr.isNotBlank()) {
                searchRepository.searchTracksByName(it)
            }else flow {
                emit(PagingData.empty()) }
        }
        .cachedIn(viewModelScope)



    fun saveSearchTerm(searchTerm: String){
        searchTermStr = searchTerm
    }

    fun findTracks() {
        this.searchTerm.value = searchTermStr
    }

    fun readErrorMessage() = errorMessage

    fun saveErrorMessage(message: String){
        errorMessage = message
    }

    fun readQuery(): String {
        val query = searchRepository.readQuery()
        searchTermStr = query
        return query
    }

    fun addTrackToFavorites(data: MediaItem) = viewModelScope.launch(dispatchersList.io()) {
        tracksRepository.checkInsertData(data).map(mapper)
    }





}