package com.kamancho.melisma.friends.data.cloud

import com.kamancho.melisma.app.vkdto.FriendItem
import com.kamancho.melisma.captcha.data.cache.CaptchaDataStore
import com.kamancho.melisma.main.data.cache.AccountDataStore
import javax.inject.Inject

/**
 * Created by HP on 17.08.2023.
 **/
interface FriendsCloudDataSource {

    suspend fun fetchFriends(): List<FriendItem>

    class Base @Inject constructor(
        private val service: FriendsService,
        private val accountDataStore: AccountDataStore,
        private val captchaDataStore: CaptchaDataStore
    ): FriendsCloudDataSource {

        companion object{
            private const val MAX_COUNT_SERVER_CAN_RETURN:Int = 5000
        }

        override suspend fun fetchFriends(): List<FriendItem> {
            val friends = mutableListOf<FriendItem>()
            var offset = 0

            while (true) {
                val friendsResponse = service.getFriends(
                    accessToken = accountDataStore.token(),
                    offset = offset,
                    captcha_sid = captchaDataStore.captchaId(),
                    captcha_key = captchaDataStore.captchaEnteredData()
                )

                val newFriends = friendsResponse.response.items
                friends.addAll(newFriends)

                if (newFriends.size < MAX_COUNT_SERVER_CAN_RETURN) {
                    break
                }

                offset += MAX_COUNT_SERVER_CAN_RETURN
            }

            return friends
        }
    }
}