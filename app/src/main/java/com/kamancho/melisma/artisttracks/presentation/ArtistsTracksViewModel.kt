package com.kamancho.melisma.artisttracks.presentation

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import com.kamancho.melisma.app.core.DispatchersList
import com.kamancho.melisma.app.core.GlobalSingleUiEventCommunication
import com.kamancho.melisma.app.core.PlayerAction
import com.kamancho.melisma.app.core.SingleUiEventState
import com.kamancho.melisma.artisttracks.domain.ArtistsTracksInteractor
import com.kamancho.melisma.main.data.TemporaryTracksCache
import com.kamancho.melisma.main.presentation.CollectPlayerControls
import com.kamancho.melisma.main.presentation.PlayerCommunication
import com.kamancho.melisma.main.presentation.PlayerCommunicationState
import com.kamancho.melisma.main.presentation.PlayerControlsState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by Ilya Boiko @camancho
on 09.12.2023.
 **/
class ArtistsTracksViewModel @Inject constructor(
    private val pageChangerCommunication: PageChangerCommunication,
    private val playerCommunication: PlayerCommunication,
    private val singleUiEventCommunication: GlobalSingleUiEventCommunication,
    private val temporaryTracksCache: TemporaryTracksCache,
    private val dispatchersList: DispatchersList,
    private val interactor: ArtistsTracksInteractor
): ViewModel(), CollectPlayerControls, PlayerAction {

    private val temporaryTracksCacheBeforeBottomSheetOpening = mutableListOf<MediaItem>()

    init {
        temporaryTracksCacheBeforeBottomSheetOpening.addAll(temporaryTracksCache.readCurrentPageTracks())
    }

    fun saveTrackId(trackId: String?) = viewModelScope.launch(dispatchersList.io()) {
        if (trackId == null) return@launch
        interactor.saveTrackId(trackId)
    }

    fun returnPreviousPageTracks() = viewModelScope.launch(Dispatchers.Main.immediate) {
        temporaryTracksCache.saveCurrentPageTracks(temporaryTracksCacheBeforeBottomSheetOpening)
    }

   override fun playerAction(action: PlayerCommunicationState) = playerCommunication.map(action)

    fun changePage(artistId: String) = pageChangerCommunication.map(artistId)

    override suspend fun collectPlayerControls(
        owner: LifecycleOwner,
        collector: FlowCollector<PlayerControlsState>,
    ) = playerCommunication.collectPlayerControls(owner,collector)

     suspend fun collectGlobalSingleUiEventCommunication(
        owner: LifecycleOwner,
        collector: FlowCollector<SingleUiEventState>,
    ) = singleUiEventCommunication.collect(owner,collector)
}