package com.kamancho.melisma.searchhistory.presentation

import com.kamancho.melisma.app.core.Communication

/**
 * Created by HP on 30.06.2023.
 **/
interface SearchHistorySingleStateCommunication: Communication.MutableSingle<SearchHistorySingleState> {

    object Base : SearchHistorySingleStateCommunication,
        Communication.SingleUiUpdate<SearchHistorySingleState>()
}