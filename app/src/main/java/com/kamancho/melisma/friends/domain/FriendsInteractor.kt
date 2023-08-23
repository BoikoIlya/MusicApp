package com.kamancho.melisma.friends.domain

import com.kamancho.melisma.app.core.HandleResponse
import com.kamancho.melisma.app.core.UpdateInteractor
import com.kamancho.melisma.friends.data.FriendsRepository
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