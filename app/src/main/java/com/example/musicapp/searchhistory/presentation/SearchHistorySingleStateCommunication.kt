package com.example.musicapp.searchhistory.presentation

import com.example.musicapp.app.core.Communication
import javax.inject.Inject

/**
 * Created by HP on 30.06.2023.
 **/
interface SearchHistorySingleStateCommunication: Communication.MutableSingle<SearchHistorySingleState> {

    object Base : SearchHistorySingleStateCommunication,
        Communication.SingleUiUpdate<SearchHistorySingleState>()
}