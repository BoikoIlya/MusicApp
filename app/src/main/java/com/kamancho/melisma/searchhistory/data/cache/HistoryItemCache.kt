package com.kamancho.melisma.searchhistory.data.cache

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
    val historyType: Int
){
    companion object{
        const val TYPE_TRACK = 0
        const val TYPE_PLAYLIST = 1
    }
}