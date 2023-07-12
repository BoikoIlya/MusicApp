package com.example.musicapp.musicdialog.presentation

import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import com.example.musicapp.app.core.DataTransfer
import com.example.musicapp.app.core.DispatchersList
import com.example.musicapp.app.core.Interactor
import com.example.musicapp.app.core.TracksResultToUiEventCommunicationMapper
import com.example.musicapp.favorites.presentation.DialogViewModel
import com.example.musicapp.favorites.presentation.TracksResult
import com.example.musicapp.trending.domain.TrackDomain
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by HP on 21.03.2023.
 **/
class AddTrackDialogViewModel @Inject constructor(
    private val dispatchersList: DispatchersList,
    private val mapper: TrackDomain.Mapper<MediaItem>,
    private val interactor: Interactor<MediaItem,TracksResult>,
    private val tracksResultToUiEventCommunicationMapper: TracksResultToUiEventCommunicationMapper,
    cache:DataTransfer<TrackDomain>,
): DialogViewModel<TrackDomain>(cache) {


    fun saveTrack() = viewModelScope.launch(dispatchersList.io()) {
        interactor.addToFavorites(super.fetchData()!!.map(mapper))
            .map(tracksResultToUiEventCommunicationMapper)
    }



}