package com.kamancho.melisma.searchhistory.data

import com.kamancho.melisma.R
import com.kamancho.melisma.app.core.ManagerResource
import com.kamancho.melisma.app.core.SearchQueryRepository
import com.kamancho.melisma.searchhistory.presentation.ToHistoryItemMapper
import com.kamancho.melisma.searchhistory.data.cache.HistoryDao
import com.kamancho.melisma.searchhistory.data.cache.HistoryItemCache
import com.kamancho.melisma.searchhistory.data.cache.SearchQueryTransfer
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject


/**
 * Created by HP on 02.05.2023.
 **/
interface SearchHistoryRepository: SearchQueryRepository {

    fun getHistoryItems(query: String,historyType: Int): Flow<List<String>>

    suspend fun clearHistory(historyType: Int): HistoryDeleteResult

    suspend fun removeHistoryItem(item: String,historyType: Int): HistoryDeleteResult

    class Base @Inject constructor(
        private val cache: HistoryDao,
        private val mapper: ToHistoryItemMapper,
        private val managerResource: ManagerResource,
        private val transfer: SearchQueryTransfer,
    ): SearchHistoryRepository, SearchQueryRepository.Abstract(transfer){

        override fun getHistoryItems(query: String,historyType: Int): Flow<List<String>> =
            if(query.isEmpty()) {
                transfer.save(Pair("",HistoryItemCache.TYPE_TRACK))
                cache.getAllHistoryByTime(historyType).map {list-> list.map { it.queryTerm } }
            }
            else cache.searchHistory(query,historyType).map {list->
                if(list.isEmpty() && query.isNotBlank()) listOf(query)
                else list.map { it.queryTerm }
            }

        override suspend fun clearHistory(historyType: Int): HistoryDeleteResult {
            val data = cache.getAllHistoryByTime(historyType).first()
            return if(data.isEmpty())
                HistoryDeleteResult.Error(managerResource.getString(R.string.the_history_is_empty))
            else {
                cache.clearHistory(historyType)
                HistoryDeleteResult.Success(data, managerResource.getString(R.string.successfully_cleared))
                }
        }

        override suspend fun removeHistoryItem(item: String,historyType: Int): HistoryDeleteResult {
            val data = cache.getAllHistoryByTime(historyType).first()
            return if(data.isEmpty())
                HistoryDeleteResult.Error(managerResource.getString(R.string.the_history_is_empty))
            else {
                cache.removeItem(item,historyType)
                HistoryDeleteResult.Success(data, managerResource.getString(R.string.success_remove_message))
            }
        }

        override suspend fun saveQueryInDB(query: String,historyType: Int) {
            super.saveQueryInDB(query,historyType)
            if(query.isNotBlank()) cache.insertHistoryItem(mapper.map(Pair(query,historyType)))
        }

    }

}