package com.example.musicapp.frienddetails.domain

import com.example.musicapp.app.core.DataTransfer
import com.example.musicapp.app.core.HandleResponse
import com.example.musicapp.app.core.TracksCacheToSelectedTracksDomainMapper
import com.example.musicapp.app.core.UpdateInteractor
import com.example.musicapp.app.core.VkException
import com.example.musicapp.favorites.data.cache.TrackCache
import com.example.musicapp.favorites.presentation.TracksResult
import com.example.musicapp.frienddetails.data.BaseFriendsDetailsPlaylistsRepository
import com.example.musicapp.frienddetails.data.FriendsDetailsRepository
import com.example.musicapp.friends.data.FriendsRepository
import com.example.musicapp.trending.domain.TrackDomain
import com.example.musicapp.userplaylists.domain.PlaylistDomain
import com.example.musicapp.userplaylists.presentation.PlaylistUi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
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