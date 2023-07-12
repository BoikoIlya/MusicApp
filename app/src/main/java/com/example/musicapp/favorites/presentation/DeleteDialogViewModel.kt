package com.example.musicapp.favorites.presentation

import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import com.example.musicapp.app.core.DataTransfer
import com.example.musicapp.app.core.DispatchersList
import com.example.musicapp.app.core.Interactor
import com.example.musicapp.app.core.TracksResultToUiEventCommunicationMapper
import com.example.musicapp.trending.domain.TrackDomain
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by HP on 26.06.2023.
 **/
class DeleteDialogViewModel @Inject constructor(
    private val dispatchersList: DispatchersList,
    private val deleteDialogCommunication: ResetSwipeActionCommunication,
    private val mapperMediaItemToId: TrackDomain.Mapper<Int>,
    private val interactor: Interactor<MediaItem,TracksResult>,
    private val tracksResultToUiEventCommunicationMapper: TracksResultToUiEventCommunicationMapper,
    cache: DataTransfer<TrackDomain>,
): DialogViewModel<TrackDomain>(cache) {

    fun removeTrack() = viewModelScope.launch(dispatchersList.io()) {
        interactor.deleteData(super.fetchData()!!.map(mapperMediaItemToId))
            .map(tracksResultToUiEventCommunicationMapper)
    }

    fun resetSwipedItem() = viewModelScope.launch(dispatchersList.io()) {
       deleteDialogCommunication.map(Unit)
    }

}