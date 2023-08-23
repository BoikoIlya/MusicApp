package com.kamancho.melisma.app.core

import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import com.kamancho.melisma.favorites.presentation.TracksResult
import com.kamancho.melisma.favorites.presentation.UiCommunication
import com.kamancho.melisma.main.data.TemporaryTracksCache
import com.kamancho.melisma.main.presentation.PlayerCommunication
import com.kamancho.melisma.main.presentation.SlideViewPagerCommunication
import kotlinx.coroutines.launch

/**
 * Created by HP on 23.04.2023.
 **/
abstract class BottomSheetPlayerViewModel (
    playerCommunication: PlayerCommunication,
    tracksInteractor: Interactor<MediaItem, TracksResult>,
    private val dispatchersList: DispatchersList,
    private val slideViewPagerCommunication: SlideViewPagerCommunication,
    mapper: TracksResultToUiEventCommunicationMapper,
    trackChecker: TrackChecker
): BaseViewModel<Unit>(
    playerCommunication,
    UiCommunication.EmptyCommunication(),
    TemporaryTracksCache.Empty,
    dispatchersList,
    tracksInteractor,
    mapper,
    trackChecker
) {

    fun slidePage(pageIndex: Int) = viewModelScope.launch(dispatchersList.io()){
        slideViewPagerCommunication.map(pageIndex)
    }


}