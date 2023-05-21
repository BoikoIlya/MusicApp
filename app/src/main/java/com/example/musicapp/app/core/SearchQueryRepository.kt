package com.example.musicapp.app.core

import com.example.musicapp.searchhistory.data.cache.SearchQueryTransfer

/**
 * Created by HP on 04.05.2023.
 **/
interface SearchQueryRepository {

    fun readQuery(): String

   suspend fun saveQuery(query: String)

    abstract class Abstract(
        private val transfer: SearchQueryTransfer
    ): SearchQueryRepository{

        override fun readQuery(): String = transfer.read()?: ""

        override suspend fun saveQuery(query: String) = transfer.save(query)

    }
}