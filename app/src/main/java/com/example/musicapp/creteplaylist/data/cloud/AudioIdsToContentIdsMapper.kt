package com.example.musicapp.creteplaylist.data.cloud

import com.example.musicapp.app.core.ManagerResource
import com.example.musicapp.app.core.Mapper
import com.example.musicapp.main.data.cache.AccountDataStore
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