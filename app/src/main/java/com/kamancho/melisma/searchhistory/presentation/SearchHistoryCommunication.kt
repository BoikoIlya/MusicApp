package com.kamancho.melisma.searchhistory.presentation

import com.kamancho.melisma.app.core.Communication
import javax.inject.Inject

/**
 * Created by HP on 02.05.2023.
 **/
interface SearchHistoryCommunication: Communication.Mutable<List<String>> {

    class Base @Inject constructor(): SearchHistoryCommunication, Communication.UiUpdate<List<String>>(
        emptyList())
}