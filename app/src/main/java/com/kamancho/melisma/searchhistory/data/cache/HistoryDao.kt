package com.kamancho.melisma.searchhistory.data.cache

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

/**
 * Created by HP on 29.04.2023.
 **/
@Dao
interface HistoryDao {

    companion object{
        const val history_table_name = "history_table"
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHistoryItem(item: HistoryItemCache)

    @Query("SELECT * FROM history_table " +
            "WHERE queryTerm LIKE '%' || :query || '%' AND historyType=:historyType " +
            "ORDER BY time DESC")
    fun searchHistory(query: String, historyType: Int): Flow<List<HistoryItemCache>>

    @Query("SELECT * FROM history_table " +
            "WHERE historyType=:historyType " +
            "ORDER BY time DESC")
    fun getAllHistoryByTime(historyType: Int): Flow<List<HistoryItemCache>>

    @Query("DELETE FROM history_table WHERE historyType=:historyType")
    suspend fun clearHistory(historyType: Int)

    @Query("DELETE FROM history_table WHERE queryTerm LIKE :id AND historyType=:historyType")
    suspend fun removeItem(id: String,historyType: Int)
}