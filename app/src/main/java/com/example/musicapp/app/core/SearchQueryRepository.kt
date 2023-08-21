package com.example.musicapp.app.core

import com.example.musicapp.searchhistory.data.cache.SearchQueryTransfer

/**
 * Created by HP on 04.05.2023.
 **/
interface SearchQueryRepository {

    fun readQueryAndHistoryType():  Pair<String,Int>

    suspend fun saveQueryInDB(query: String,historyType: Int)

    abstract class Abstract(
        private val transfer: SearchQueryTransfer
    ): SearchQueryRepository{

        override fun readQueryAndHistoryType(): Pair<String,Int> = transfer.read()?: Pair("",0)

        override suspend fun saveQueryInDB(query: String,historyType: Int) = transfer.save(Pair(query,historyType))

    }
}