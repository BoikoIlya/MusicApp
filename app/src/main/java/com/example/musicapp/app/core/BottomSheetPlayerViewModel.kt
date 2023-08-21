package com.example.musicapp.app.core

import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import com.example.musicapp.favorites.presentation.TracksResult
import com.example.musicapp.favorites.presentation.UiCommunication
import com.example.musicapp.main.data.TemporaryTracksCache
import com.example.musicapp.main.presentation.PlayerCommunication
import com.example.musicapp.main.presentation.SlideViewPagerCommunication
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