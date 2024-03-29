package com.example.musicapp.searchhistory.data.cache

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Created by HP on 29.04.2023.
 **/
@Entity(tableName = HistoryDao.history_table_name)
data class HistoryItemCache(
    @PrimaryKey(autoGenerate = false)
    val queryTerm: String,
    val time: Long,
)