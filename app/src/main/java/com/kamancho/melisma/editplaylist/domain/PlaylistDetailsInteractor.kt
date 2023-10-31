package com.kamancho.melisma.editplaylist.domain

import com.kamancho.melisma.app.core.DataTransfer
import com.kamancho.melisma.app.core.HandleResponse
import com.kamancho.melisma.app.core.UpdateInteractor
import com.kamancho.melisma.editplaylist.data.PlaylistDetailsRepository
import com.kamancho.melisma.userplaylists.domain.PlaylistDomain
import javax.inject.Inject

/**
 * Created by HP on 18.07.2023.
 **/
interface PlaylistDetailsInteractor: UpdateInteractor {

    class Base @Inject constructor(
        private val repository: PlaylistDetailsRepository,
        private val handleResponse: HandleResponse,
    ): PlaylistDetailsInteractor {

        override suspend fun update(ownerId: Int,playlistId: String): String = handleResponse.handle({
            repository.fetch(playlistId,ownerId)
            ""
        },{ message, _ ->
            message
        })

    }
}