package com.example.musicapp.app.core

import androidx.lifecycle.viewModelScope
import com.example.musicapp.favorites.data.cache.TrackCache
import com.example.musicapp.favorites.presentation.TracksCommunication
import com.example.musicapp.main.data.TemporaryTracksCache
import com.example.musicapp.main.presentation.PlayerCommunication
import com.example.musicapp.main.presentation.SlideViewPagerCommunication
import kotlinx.coroutines.launch

/**
 * Created by HP on 23.04.2023.
 **/
abstract class BottomSheetPlayerViewModel (
    playerCommunication: PlayerCommunication,
    tracksCache: TemporaryTracksCache,
    tracksRepository: TracksRepository,
    private val dispatchersList: DispatchersList,
    private val slideViewPagerCommunication: SlideViewPagerCommunication
): BaseViewModel<Unit>(
    playerCommunication,
    TracksCommunication.EmptyCommunication(),
    tracksCache,
    dispatchersList,
    tracksRepository,
    TracksResultEmptyMapper()
) {

    fun slidePage(pageIndex: Int) = viewModelScope.launch(dispatchersList.io()){
        slideViewPagerCommunication.map(pageIndex)
    }
}