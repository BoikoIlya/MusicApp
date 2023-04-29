package com.example.musicapp.searchhistory.data.cache

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Created by HP on 29.04.2023.
 **/
@Entity(tableName = HistoryDao.history_table_name)
data class HistoryItemCache(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val queryTerm: String,
    val time: Long
)