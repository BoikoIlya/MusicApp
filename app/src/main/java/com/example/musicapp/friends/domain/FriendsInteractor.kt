package com.example.musicapp.friends.domain

import com.example.musicapp.app.core.HandleResponse
import com.example.musicapp.app.core.UpdateInteractor
import com.example.musicapp.friends.data.FriendsRepository
import com.example.musicapp.friends.data.cache.FriendCache
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * Created by HP on 17.08.2023.
 **/
interface FriendsInteractor: UpdateInteractor {


    suspend fun search(query: String): Flow<List<FriendDomain>>

    class Base @Inject constructor(
        private val repository: FriendsRepository,
        private val handleResponse: HandleResponse,
        private val mapper: FriendsCacheToFriendsDomainMapper
    ): FriendsInteractor {


        override suspend fun search(query: String):Flow<List<FriendDomain>> =
            repository.search(query).map {  mapper.map(it)}

        override suspend fun updateData(): String  = handleResponse.handle({
            repository.updateFriends()
            ""
        },{message,_->
            return@handle message
        })
    }
}