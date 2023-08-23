package com.kamancho.melisma.frienddetails.domain

import com.kamancho.melisma.app.core.DataTransfer
import com.kamancho.melisma.app.core.HandleResponse
import com.kamancho.melisma.app.core.UpdateInteractor
import com.kamancho.melisma.frienddetails.data.BaseFriendsDetailsPlaylistsRepository
import com.kamancho.melisma.userplaylists.domain.PlaylistDomain
import com.kamancho.melisma.userplaylists.presentation.PlaylistUi
import javax.inject.Inject

/**
 * Created by HP on 18.08.2023.
 **/
interface FriendDetailsPlaylistsInteractor: UpdateInteractor {

    fun savePlaylistData(item: PlaylistUi)

     class Base @Inject constructor(
         private val repository: BaseFriendsDetailsPlaylistsRepository,
         private val handleResponse: HandleResponse,
         private val transfer: FriendIdAndNameTransfer,
         private val playlistTransfer: DataTransfer<PlaylistDomain>,
         private val mapper: PlaylistUi.Mapper<PlaylistDomain>
    ): FriendDetailsPlaylistsInteractor{

         override fun savePlaylistData(item: PlaylistUi) {
             playlistTransfer.save(item.map(mapper))
         }

         override suspend fun updateData(): String = handleResponse.handle({
            repository.update(transfer.read()!!.first)
            ""
        },{message,_->
            message
        })

    }

}