package com.kamancho.melisma.creteplaylist.data.cloud

import com.kamancho.melisma.main.data.cache.AccountDataStore
import javax.inject.Inject

/**
 * Created by HP on 18.07.2023.
 **/
interface AudioIdsToContentIdsMapper{

    suspend fun map(audioIds: List<Int>): String

    class Base @Inject constructor(
        private val accountDataStore: AccountDataStore
    ): AudioIdsToContentIdsMapper {

        override suspend fun map(audioIds: List<Int>): String {
            val ownerId = accountDataStore.ownerId()
            return audioIds.joinToString { ownerId+"_"+it+", " }
        }
    }
}