package com.kamancho.melisma.friends.domain

import com.kamancho.melisma.app.core.Mapper
import com.kamancho.melisma.friends.data.cache.FriendCache
import javax.inject.Inject

/**
 * Created by HP on 17.08.2023.
 **/
interface FriendsCacheToFriendsDomainMapper: Mapper<List<FriendCache>, List<FriendDomain>> {

    class Base @Inject constructor(): FriendsCacheToFriendsDomainMapper {
        override fun map(data: List<FriendCache>): List<FriendDomain> {
            return data.map { FriendDomain(
                id = it.friendId,
                firstName = it.firstName,
                secondName = it.secondName,
                photoUrl = it.photoUrl
            ) }
        }
    }
}