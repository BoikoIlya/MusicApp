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
        private val transfer: DataTransfer<PlaylistDomain>,
        private val toOwnerIdAndPlaylistIdMapper: PlaylistDomain.Mapper<Pair<Int,String>>
    ): PlaylistDetailsInteractor {

        override suspend fun updateData(): String = handleResponse.handle({
            val ownerIdAndPlaylistId = transfer.read()!!.map(toOwnerIdAndPlaylistIdMapper)
            repository.fetch(ownerIdAndPlaylistId.second, ownerIdAndPlaylistId.first)
            ""
        },{ message, _ ->
            message
        })

    }
}