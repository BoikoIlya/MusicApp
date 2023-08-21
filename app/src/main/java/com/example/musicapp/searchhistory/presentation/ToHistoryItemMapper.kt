package com.example.musicapp.searchhistory.presentation

import android.util.Log
import com.example.musicapp.app.core.Mapper
import com.example.musicapp.searchhistory.data.cache.HistoryItemCache
import java.time.Instant
import java.util.Calendar
import javax.inject.Inject

/**
 * Created by HP on 02.05.2023.
 **/
interface ToHistoryItemMapper: Mapper<Pair<String,Int>, HistoryItemCache> {

    class Base @Inject constructor(): ToHistoryItemMapper {

        override fun map(data: Pair<String,Int>): HistoryItemCache {
            return HistoryItemCache(
                time = Instant.now().toEpochMilli(),
                queryTerm = data.first,
                historyType = data.second
            )
        }
    }

}