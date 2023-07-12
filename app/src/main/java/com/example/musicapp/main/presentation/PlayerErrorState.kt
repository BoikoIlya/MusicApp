package com.example.musicapp.main.presentation

import com.example.musicapp.app.core.DispatchersList
import com.example.musicapp.app.core.GlobalSingleUiEventCommunication
import com.example.musicapp.app.core.SingleUiEventState
import com.example.musicapp.trending.presentation.MediaControllerWrapper
import kotlinx.coroutines.withContext

/**
 * Created by HP on 03.07.2023.
 **/
interface PlayerErrorState {

   suspend fun apply(
        globalSingleUiEventCommunication: GlobalSingleUiEventCommunication,
        dispatchersList: DispatchersList,
        controller: MediaControllerWrapper,
    )

    abstract class Abstract(
        private val message: String
    ): PlayerErrorState{

        override suspend fun apply(
            globalSingleUiEventCommunication: GlobalSingleUiEventCommunication,
            dispatchersList: DispatchersList,
            controller: MediaControllerWrapper,
        ) {
            globalSingleUiEventCommunication.map(SingleUiEventState.ShowSnackBar.Error(message))
        }
    }

    data class ShowError(
       private val message: String
       ) : Abstract(message){

    }

    data class SeekToNext(
        private val message: String
    ): Abstract(message){

        override suspend fun apply(
            globalSingleUiEventCommunication: GlobalSingleUiEventCommunication,
            dispatchersList: DispatchersList,
            controller: MediaControllerWrapper,
        ) {
            super.apply(globalSingleUiEventCommunication, dispatchersList,controller)

            withContext(dispatchersList.ui()) {
                controller.seekToNextMediaItem()
                controller.play()
            }
        }
    }
}