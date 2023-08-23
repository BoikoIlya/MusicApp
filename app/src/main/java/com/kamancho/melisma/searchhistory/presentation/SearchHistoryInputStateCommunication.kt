package com.kamancho.melisma.searchhistory.presentation

import com.kamancho.melisma.app.core.Communication
import javax.inject.Inject

/**
 * Created by HP on 30.06.2023.
 **/
interface SearchHistoryInputStateCommunication: Communication.Mutable<SearchHistoryTextInputState> {

    class Base @Inject constructor(): SearchHistoryInputStateCommunication,
        Communication.UiUpdate<SearchHistoryTextInputState>(SearchHistoryTextInputState.Nothing)
}