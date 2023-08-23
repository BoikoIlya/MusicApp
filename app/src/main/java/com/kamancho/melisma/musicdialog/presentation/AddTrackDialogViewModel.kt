package com.kamancho.melisma.musicdialog.presentation

import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import com.kamancho.melisma.app.core.DataTransfer
import com.kamancho.melisma.app.core.DispatchersList
import com.kamancho.melisma.app.core.Interactor
import com.kamancho.melisma.app.core.TracksResultToUiEventCommunicationMapper
import com.kamancho.melisma.favorites.presentation.DialogViewModel
import com.kamancho.melisma.favorites.presentation.TracksResult
import com.kamancho.melisma.trending.domain.TrackDomain
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