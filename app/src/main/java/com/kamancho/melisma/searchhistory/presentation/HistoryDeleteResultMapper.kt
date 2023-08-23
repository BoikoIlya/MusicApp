package com.kamancho.melisma.searchhistory.presentation

import com.kamancho.melisma.app.core.GlobalSingleUiEventCommunication
import com.kamancho.melisma.app.core.SingleUiEventState
import com.kamancho.melisma.searchhistory.data.HistoryDeleteResult
import com.kamancho.melisma.searchhistory.data.cache.HistoryItemCache
import javax.inject.Inject

/**
 * Created by HP on 06.05.2023.
 **/
class HistoryDeleteResultMapper @Inject constructor(
    private val singleUiEventCommunication: GlobalSingleUiEventCommunication
): HistoryDeleteResult.Mapper<Unit> {

    override suspend fun map(message: String, data: List<HistoryItemCache>) {
        if(data.isEmpty()){
            singleUiEventCommunication.map(SingleUiEventState.ShowSnackBar.Error(message))
        } else singleUiEventCommunication.map(SingleUiEventState.ShowSnackBar.Success(message))
    }
}