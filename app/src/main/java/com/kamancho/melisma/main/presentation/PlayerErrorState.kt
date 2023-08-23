package com.kamancho.melisma.main.presentation

import com.kamancho.melisma.app.core.DispatchersList
import com.kamancho.melisma.app.core.GlobalSingleUiEventCommunication
import com.kamancho.melisma.app.core.SingleUiEventState
import com.kamancho.melisma.trending.presentation.MediaControllerWrapper
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