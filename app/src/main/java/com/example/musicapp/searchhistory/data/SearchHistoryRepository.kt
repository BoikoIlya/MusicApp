package com.example.musicapp.searchhistory.data

import com.example.musicapp.R
import com.example.musicapp.app.core.ManagerResource
import com.example.musicapp.app.core.SearchQueryRepository
import com.example.musicapp.searchhistory.presentation.ToHistoryItemMapper
import com.example.musicapp.searchhistory.data.cache.HistoryDao
import com.example.musicapp.searchhistory.data.cache.SearchQueryTransfer
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject


/**
 * Created by HP on 02.05.2023.
 **/
interface SearchHistoryRepository: SearchQueryRepository {

    fun getHistoryItems(query: String): Flow<List<String>>

    suspend fun clearHistory(): HistoryDeleteResult

    suspend fun removeHistoryItem(item: String): HistoryDeleteResult

    class Base @Inject constructor(
        private val cache: HistoryDao,
        private val mapper: ToHistoryItemMapper,
        private val managerResource: ManagerResource,
        transfer: SearchQueryTransfer
    ): SearchHistoryRepository, SearchQueryRepository.Abstract(transfer){


        override fun getHistoryItems(query: String): Flow<List<String>> =
            if(query.isEmpty()) cache.getAllHistoryByTime().map {list-> list.map { it.queryTerm } }
            else cache.searchHistory(query).map {list->
                if(list.isEmpty() && query.isNotBlank()) listOf(query)
                else list.map { it.queryTerm }
            }

        override suspend fun clearHistory(): HistoryDeleteResult {
            val data = cache.getAllHistoryByTime().first()
            return if(data.isEmpty())
                HistoryDeleteResult.Error(managerResource.getString(R.string.the_history_is_empty))
            else {
                cache.clearHistory()
                HistoryDeleteResult.Success(data, managerResource.getString(R.string.successfully_cleared))
                }
        }




        override suspend fun removeHistoryItem(item: String): HistoryDeleteResult {
            val data = cache.getAllHistoryByTime().first()
            return if(data.isEmpty())
                HistoryDeleteResult.Error(managerResource.getString(R.string.the_history_is_empty))
            else {
                cache.removeItem(item)
                HistoryDeleteResult.Success(data, managerResource.getString(R.string.success_remove_message))
            }
        }

        override suspend fun saveQuery(query: String) {
            super.saveQuery(query)
            if(query.isNotBlank()) cache.insertHistoryItem(mapper.map(query))
        }
    }

}