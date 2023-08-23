package com.kamancho.melisma.searchhistory.presentation

import com.kamancho.melisma.app.core.Communication
import javax.inject.Inject

/**
 * Created by HP on 13.08.2023.
 **/
interface SearchQueryCommunication: Communication.Mutable<String> {

    class Base @Inject constructor(): SearchQueryCommunication, Communication.UiUpdate<String>("")
}