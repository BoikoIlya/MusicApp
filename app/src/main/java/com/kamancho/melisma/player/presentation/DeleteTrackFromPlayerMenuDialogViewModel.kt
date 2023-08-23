package com.kamancho.melisma.player.presentation

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
 * Created by HP on 02.07.2023.
 **/
class DeleteTrackFromPlayerMenuDialogViewModel @Inject constructor(
    private val interactor: Interactor<MediaItem,TracksResult>,
    private val dispatchersList: DispatchersList,
    private val tracksResultToUiEventCommunicationMapper: TracksResultToUiEventCommunicationMapper,
    private val mapperMediaItemToId: TrackDomain.Mapper<Int>,
    cache: DataTransfer<TrackDomain>
): DialogViewModel<TrackDomain>(cache) {

     fun deleteTrackFromFavorites() = viewModelScope.launch(dispatchersList.io()) {
            interactor.deleteData(super.fetchData()!!.map(mapperMediaItemToId))
                .map(tracksResultToUiEventCommunicationMapper)
        }


}