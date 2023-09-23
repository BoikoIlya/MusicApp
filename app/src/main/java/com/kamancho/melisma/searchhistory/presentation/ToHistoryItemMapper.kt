package com.kamancho.melisma.searchhistory.presentation

import com.kamancho.melisma.app.core.Mapper
import com.kamancho.melisma.searchhistory.data.cache.HistoryItemCache
import java.time.Instant
import javax.inject.Inject

/**
 * Created by HP on 02.05.2023.
 **/
interface ToHistoryItemMapper: Mapper<Pair<String,Int>, HistoryItemCache> {

    class Base @Inject constructor(): ToHistoryItemMapper {

        override fun map(data: Pair<String,Int>): HistoryItemCache {
            return HistoryItemCache(
                time = System.currentTimeMillis(),
                queryTerm = data.first,
                historyType = data.second
            )
        }
    }

}