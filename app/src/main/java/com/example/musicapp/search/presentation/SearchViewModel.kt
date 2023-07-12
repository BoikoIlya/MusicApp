package com.example.musicapp.search.presentation

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.musicapp.app.core.BaseViewModel
import com.example.musicapp.app.core.DispatchersList
import com.example.musicapp.app.core.Interactor
import com.example.musicapp.app.core.TrackChecker
import com.example.musicapp.app.core.TracksResultToUiEventCommunicationMapper
import com.example.musicapp.favorites.domain.FavoritesTracksInteractor
import com.example.musicapp.favorites.presentation.TracksResult
import com.example.musicapp.favorites.presentation.UiCommunication
import com.example.musicapp.main.data.TemporaryTracksCache
import com.example.musicapp.main.presentation.PlayerCommunication
import com.example.musicapp.search.data.SearchRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
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
    private val favoritesInteractor: Interactor<MediaItem, TracksResult>,
    private val mapper: TracksResultToUiEventCommunicationMapper,
    trackChecker: TrackChecker
): BaseViewModel<Unit>(
    playerCommunication,
    UiCommunication.EmptyCommunication(),
    temporaryTracksCache,
    dispatchersList,
    favoritesInteractor,
    mapper,
    trackChecker
){

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


}