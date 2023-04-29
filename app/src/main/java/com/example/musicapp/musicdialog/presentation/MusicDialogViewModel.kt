package com.example.musicapp.musicdialog.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import com.example.musicapp.app.core.DataTransfer
import com.example.musicapp.app.core.DispatchersList
import com.example.musicapp.app.core.TracksRepository
import com.example.musicapp.app.core.UiEventState
import com.example.musicapp.favorites.presentation.TracksResultToSingleUiEventCommunicationMapper
import com.example.musicapp.main.presentation.UiEventsCommunication
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by HP on 21.03.2023.
 **/
class MusicDialogViewModel @Inject constructor(
    private val cache: DataTransfer<MediaItem>,
    private val repository: TracksRepository,
    private val dispatchersList: DispatchersList,
    private val mapper: TracksResultToSingleUiEventCommunicationMapper,
    private val uiEventsCommunication: UiEventsCommunication
): ViewModel() {

    fun fetchData() = cache.read()

    fun saveTrack(data: MediaItem) = viewModelScope.launch(dispatchersList.io()) {
        uiEventsCommunication.map(UiEventState.ClearCommunication)
        repository.insertData(data).map(mapper)
    }

}