package com.kamancho.melisma.search.presentation

import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.kamancho.melisma.app.core.BaseViewModel
import com.kamancho.melisma.app.core.DispatchersList
import com.kamancho.melisma.app.core.Interactor
import com.kamancho.melisma.app.core.TrackChecker
import com.kamancho.melisma.app.core.TracksResultToUiEventCommunicationMapper
import com.kamancho.melisma.favorites.presentation.TracksResult
import com.kamancho.melisma.favorites.presentation.UiCommunication
import com.kamancho.melisma.main.data.TemporaryTracksCache
import com.kamancho.melisma.main.presentation.PlayerCommunication
import com.kamancho.melisma.search.data.SearchRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow

/**
 * Created by HP on 01.05.2023.
 **/
@ExperimentalCoroutinesApi
@FlowPreview
abstract class SearchListViewModel<T:Any> (
    private val searchRepository: SearchRepository<T>,
    playerCommunication: PlayerCommunication,
    dispatchersList: DispatchersList,
    temporaryTracksCache: TemporaryTracksCache,
    favoritesInteractor: Interactor<MediaItem, TracksResult>,
    mapper: TracksResultToUiEventCommunicationMapper,
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

    init {
        searchTermStr = searchRepository.readQueryAndHistoryType().first
        this.searchTerm.value = searchTermStr
    }

    val list: Flow<PagingData<T>> = searchTerm
        .flatMapLatest {
            if(searchTermStr.isNotBlank()) {
                searchRepository.search(it)
            }else flow {
                emit(PagingData.empty()) }
        }
        .cachedIn(viewModelScope)



    fun readErrorMessage() = errorMessage

    fun saveErrorMessage(message: String){
        errorMessage = message
    }


}



