package com.kamancho.melisma.frienddetails.domain

import com.kamancho.melisma.app.core.HandleResponse
import com.kamancho.melisma.app.core.UpdateInteractor
import com.kamancho.melisma.frienddetails.data.BaseFriendsDetailsTracksRepository
import javax.inject.Inject

/**
 * Created by HP on 18.08.2023.
 **/
interface FriendDetailsTracksInteractor: UpdateInteractor {



    class Base @Inject constructor(
        private val repository: BaseFriendsDetailsTracksRepository,
        private val handleResponse: HandleResponse,
    ): FriendDetailsTracksInteractor{

        override suspend fun update(id: String):String = handleResponse.handle({
            repository.update(id)
            ""
        },{message,_->
            message
        })


    }
}