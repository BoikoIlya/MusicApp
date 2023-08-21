package com.example.musicapp.frienddetails.domain

import com.example.musicapp.app.core.HandleResponse
import com.example.musicapp.app.core.TracksCacheToSelectedTracksDomainMapper
import com.example.musicapp.app.core.UpdateInteractor
import com.example.musicapp.app.core.VkException
import com.example.musicapp.favorites.data.cache.TrackCache
import com.example.musicapp.favorites.presentation.TracksResult
import com.example.musicapp.frienddetails.data.BaseFriendsDetailsTracksRepository
import com.example.musicapp.frienddetails.data.FriendsDetailsRepository
import com.example.musicapp.friends.data.FriendsRepository
import com.example.musicapp.trending.domain.TrackDomain
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * Created by HP on 18.08.2023.
 **/
interface FriendDetailsTracksInteractor: UpdateInteractor {



    class Base @Inject constructor(
        private val repository: BaseFriendsDetailsTracksRepository,
        private val handleResponse: HandleResponse,
        private val transfer: FriendIdAndNameTransfer,
    ): FriendDetailsTracksInteractor{



        override suspend fun updateData(): String = handleResponse.handle({
            repository.update(transfer.read()!!.first)
            ""
        },{message,_->
            message
        })

    }
}