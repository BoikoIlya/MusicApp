package com.example.musicapp.search.presentation

import androidx.media3.common.MediaItem
import com.example.musicapp.app.core.DispatchersList
import com.example.musicapp.app.core.Interactor
import com.example.musicapp.app.core.TrackChecker
import com.example.musicapp.app.core.TracksResultToUiEventCommunicationMapper
import com.example.musicapp.favorites.presentation.TracksResult
import com.example.musicapp.main.data.TemporaryTracksCache
import com.example.musicapp.main.presentation.PlayerCommunication
import com.example.musicapp.search.data.SearchRepository
import javax.inject.Inject

/**
 * Created by HP on 14.08.2023.
 **/
class BaseTracksListSearchViewModel @Inject constructor(
    private val searchRepository: SearchRepository<MediaItem>,
    playerCommunication: PlayerCommunication,
    private val dispatchersList: DispatchersList,
    temporaryTracksCache: TemporaryTracksCache,
    private val favoritesInteractor: Interactor<MediaItem, TracksResult>,
    private val mapper: TracksResultToUiEventCommunicationMapper,
    trackChecker: TrackChecker
): SearchListViewModel<MediaItem>(
    searchRepository,
    playerCommunication,
    dispatchersList,
    temporaryTracksCache,
    favoritesInteractor,
    mapper,
    trackChecker
)