package com.example.musicapp.searchhistory.data.cache

import androidx.room.Dao

/**
 * Created by HP on 29.04.2023.
 **/
@Dao
interface HistoryDao {

    companion object{
        const val history_table_name = "history_table"
    }
}