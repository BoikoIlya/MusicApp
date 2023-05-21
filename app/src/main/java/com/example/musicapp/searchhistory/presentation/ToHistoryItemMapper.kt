package com.example.musicapp.searchhistory.presentation

import com.example.musicapp.app.core.Mapper
import com.example.musicapp.searchhistory.data.cache.HistoryItemCache
import java.util.Calendar
import javax.inject.Inject

/**
 * Created by HP on 02.05.2023.
 **/
interface ToHistoryItemMapper: Mapper<String, HistoryItemCache> {

    class Base @Inject constructor(): ToHistoryItemMapper {
        private val calendar = Calendar.getInstance()

        override fun map(data: String): HistoryItemCache {
            return HistoryItemCache(time = calendar.timeInMillis, queryTerm =  data)
        }
    }

}