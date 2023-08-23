package com.kamancho.melisma.favorites.presentation

import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import com.kamancho.melisma.app.core.DataTransfer
import com.kamancho.melisma.app.core.DispatchersList
import com.kamancho.melisma.app.core.Interactor
import com.kamancho.melisma.app.core.TracksResultToUiEventCommunicationMapper
import com.kamancho.melisma.trending.domain.TrackDomain
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