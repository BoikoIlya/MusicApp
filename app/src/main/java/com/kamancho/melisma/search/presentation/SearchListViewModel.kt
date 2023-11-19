package com.kamancho.melisma.search.presentation

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import com.kamancho.melisma.app.core.BaseViewModel
import com.kamancho.melisma.app.core.Communication
import com.kamancho.melisma.app.core.DispatchersList
import com.kamancho.melisma.app.core.Interactor
import com.kamancho.melisma.app.core.PagingLoadState
import com.kamancho.melisma.app.core.TrackChecker
import com.kamancho.melisma.app.core.TracksResultToUiEventCommunicationMapper
import com.kamancho.melisma.favorites.presentation.TracksResult
import com.kamancho.melisma.favorites.presentation.UiCommunication
import com.kamancho.melisma.main.data.TemporaryTracksCache
import com.kamancho.melisma.main.presentation.PlayerCommunication
import com.kamancho.melisma.search.domain.SearchDomainState
import com.kamancho.melisma.search.domain.SearchInteractor
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.launch

/**
 * Created by HP on 01.05.2023.
 **/

abstract class SearchListViewModel<T,R,N> (
    private val interactor: SearchInteractor<T,R>,
    private val mapperToUi: SearchDomainState.Mapper<R>,
    private val dataListCommunication: SearchListCommunication<N>,
    private val loadStateCommunication: Communication.Mutable<PagingLoadState>,
    playerCommunication: PlayerCommunication,
    private val dispatchersList: DispatchersList,
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

    private var firstLoadAfterInit = false

    init {
        firstLoadAfterInit = true
    }

    fun loadPage(query: String, shouldLoad: Boolean) = viewModelScope.launch(dispatchersList.io()) {
        if(!shouldLoad && !firstLoadAfterInit) return@launch
        firstLoadAfterInit = false

        loadStateCommunication.map(PagingLoadState.Loading)
        interactor.load(query).map(mapperToUi)
    }

    suspend fun collectDataListCommunication(
        lifecycleOwner: LifecycleOwner,
        flowCollector: FlowCollector<List<N>>
    ) = dataListCommunication.collect(lifecycleOwner,flowCollector)

    suspend fun collectLoadStateCommunication(
        lifecycleOwner: LifecycleOwner,
        flowCollector: FlowCollector<PagingLoadState>
    ) = loadStateCommunication.collect(lifecycleOwner,flowCollector)


}



