package com.kamancho.melisma.artisttracks.presentation

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import com.kamancho.melisma.app.core.BaseViewModel
import com.kamancho.melisma.app.core.DispatchersList
import com.kamancho.melisma.app.core.Interactor
import com.kamancho.melisma.app.core.TrackChecker
import com.kamancho.melisma.app.core.TracksResultToUiEventCommunicationMapper
import com.kamancho.melisma.artisttracks.domain.ArtistsTracksInteractor
import com.kamancho.melisma.artisttracks.domain.TracksDomainState
import com.kamancho.melisma.favorites.presentation.TracksResult
import com.kamancho.melisma.main.data.TemporaryTracksCache
import com.kamancho.melisma.main.presentation.PlayerCommunication
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by Ilya Boiko @camancho
on 09.12.2023.
 **/
class ArtistTracksListViewModel @Inject constructor(
    playerCommunication: PlayerCommunication,
    private val communication: ArtistTracksCommunication,
    temporaryTracksCache: TemporaryTracksCache,
    private val dispatchersList: DispatchersList,
    favoritesInteractor: Interactor<MediaItem, TracksResult>,
    mapper: TracksResultToUiEventCommunicationMapper,
    trackChecker: TrackChecker,
    private val interactor: ArtistsTracksInteractor,
    private val domainToUiMapper: TracksDomainState.Mapper<Unit>,
    private val pageChangerCommunication: PageChangerCommunication
) : BaseViewModel<ArtistsTracksUiState>(
    playerCommunication,
    communication,
    temporaryTracksCache,
    dispatchersList,
    favoritesInteractor,
    mapper,
    trackChecker
) {

    private var firstLoadAfterInit = true

    fun loadTracks(artistId: String?,isIdExist: Boolean) = viewModelScope.launch(dispatchersList.io()){
        if(artistId==null || !firstLoadAfterInit) return@launch
        firstLoadAfterInit = false

        communication.showUiState(ArtistsTracksUiState.Loading)
        interactor.fetchTracks(if(isIdExist) artistId.trim() else "").map(domainToUiMapper)
    }

    fun reloadTracks(artistId: String?,isIdExist: Boolean){
        firstLoadAfterInit = true
        loadTracks(artistId, isIdExist)
    }

    suspend fun collectPageChangerCommunication(
        owner: LifecycleOwner,
        collector: FlowCollector<String>,
    ) = pageChangerCommunication.collect(owner, collector)
}