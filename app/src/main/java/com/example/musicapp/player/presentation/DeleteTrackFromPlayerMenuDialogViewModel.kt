package com.example.musicapp.player.presentation

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import com.example.musicapp.app.core.DataTransfer
import com.example.musicapp.app.core.DispatchersList
import com.example.musicapp.app.core.Interactor
import com.example.musicapp.app.core.TracksResultToUiEventCommunicationMapper
import com.example.musicapp.favorites.presentation.DialogViewModel
import com.example.musicapp.favorites.presentation.TracksResult
import com.example.musicapp.trending.domain.TrackDomain
import kotlinx.coroutines.flow.FlowCollector
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