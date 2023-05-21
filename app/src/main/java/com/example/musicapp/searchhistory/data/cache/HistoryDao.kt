package com.example.musicapp.searchhistory.data.cache

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.google.api.ResourceDescriptor.History
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

    @Query("SELECT * FROM history_table WHERE queryTerm LIKE '%' || :query || '%' ORDER BY time DESC")
    fun searchHistory(query: String): Flow<List<HistoryItemCache>>

    @Query("SELECT * FROM history_table  ORDER BY time DESC")
    fun getAllHistoryByTime(): Flow<List<HistoryItemCache>>

    @Query("DELETE FROM history_table")
    suspend fun clearHistory()

    @Query("DELETE FROM history_table WHERE queryTerm LIKE :id")
    suspend fun removeItem(id: String)
}