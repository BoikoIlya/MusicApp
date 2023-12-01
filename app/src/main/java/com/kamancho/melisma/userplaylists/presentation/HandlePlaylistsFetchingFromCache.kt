package com.kamancho.melisma.userplaylists.presentation

import com.kamancho.melisma.app.core.DispatchersList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by HP on 20.07.2023.
 **/
interface HandlePlaylistsFetchingFromCache {

    fun fetch(scope: CoroutineScope,block: suspend ()->Unit)

     class Base @Inject constructor(
        private val dispatchersList: DispatchersList,
    ): HandlePlaylistsFetchingFromCache{

        private var fetching: Job? = null

        override fun fetch(scope: CoroutineScope,block: suspend ()->Unit) {
            fetching?.cancel()

            fetching = scope.launch(dispatchersList.io()) {
                block.invoke()
            }
        }
    }
}