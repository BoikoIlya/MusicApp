package com.example.musicapp.searchhistory.presentation

import com.example.musicapp.app.core.SingleUiEventCommunication
import com.example.musicapp.app.core.SingleUiEventState
import com.example.musicapp.searchhistory.data.HistoryDeleteResult
import com.example.musicapp.searchhistory.data.cache.HistoryItemCache
import javax.inject.Inject

/**
 * Created by HP on 06.05.2023.
 **/
class HistoryDeleteResultMapper @Inject constructor(
    private val singleUiEventCommunication: SingleUiEventCommunication
): HistoryDeleteResult.Mapper<Unit> {

    override suspend fun map(message: String, data: List<HistoryItemCache>) {
        if(data.isEmpty()){
            singleUiEventCommunication.map(SingleUiEventState.ShowSnackBar.Error(message))
        } else singleUiEventCommunication.map(SingleUiEventState.ShowSnackBar.Success(message))
    }
}