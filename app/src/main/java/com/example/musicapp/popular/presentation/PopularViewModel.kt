package com.example.musicapp.popular.presentation

import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import com.example.musicapp.app.core.BaseViewModel
import com.example.musicapp.app.core.DispatchersList
import com.example.musicapp.app.core.Interactor
import com.example.musicapp.app.core.TrackChecker
import com.example.musicapp.app.core.TracksResultToUiEventCommunicationMapper
import com.example.musicapp.favorites.presentation.FavoritesUiState
import com.example.musicapp.favorites.presentation.TracksResult
import com.example.musicapp.favorites.presentation.UiCommunication
import com.example.musicapp.main.data.TemporaryTracksCache
import com.example.musicapp.main.presentation.PlayerCommunication
import com.example.musicapp.popular.domain.PopularInteractor
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by HP on 22.08.2023.
 **/
class PopularViewModel @Inject constructor(
    playerCommunication: PlayerCommunication,
    private val dispatchersList: DispatchersList,
    temporaryTracksCache: TemporaryTracksCache,
    favoritesInteractor: Interactor<MediaItem, TracksResult>,
    mapper: TracksResultToUiEventCommunicationMapper,
    trackChecker: TrackChecker,
    private val communication: PopularCommunication,
    private val interactor: PopularInteractor,
    private val tracksResultToPopularTracksCommunicationMapper: TracksResultToPopularTracksCommunicationMapper
): BaseViewModel<FavoritesUiState>(
    playerCommunication,
    communication,
    temporaryTracksCache,
    dispatchersList,
    favoritesInteractor,
    mapper,
    trackChecker
) {

    init {
        update(false)
    }

    override fun update(loading: Boolean) {
        communication.showUiState(FavoritesUiState.Loading)
        viewModelScope.launch(dispatchersList.io()) {
           val result = interactor.update()
            result.map(tracksResultToPopularTracksCommunicationMapper)
        }
    }
}