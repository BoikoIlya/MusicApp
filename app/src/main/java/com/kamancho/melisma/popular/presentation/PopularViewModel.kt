package com.kamancho.melisma.popular.presentation

import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import com.kamancho.melisma.app.core.BaseViewModel
import com.kamancho.melisma.app.core.DispatchersList
import com.kamancho.melisma.app.core.Interactor
import com.kamancho.melisma.app.core.TrackChecker
import com.kamancho.melisma.app.core.TracksResultToUiEventCommunicationMapper
import com.kamancho.melisma.favorites.presentation.FavoritesUiState
import com.kamancho.melisma.favorites.presentation.TracksResult
import com.kamancho.melisma.main.data.TemporaryTracksCache
import com.kamancho.melisma.main.presentation.PlayerCommunication
import com.kamancho.melisma.popular.domain.PopularInteractor
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