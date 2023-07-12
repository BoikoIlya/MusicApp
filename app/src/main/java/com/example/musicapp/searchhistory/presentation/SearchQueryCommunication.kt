package com.example.musicapp.searchhistory.presentation

import com.example.musicapp.app.core.Communication
import javax.inject.Inject

/**
 * Created by HP on 30.06.2023.
 **/
interface SearchQueryCommunication: Communication.Mutable<String> {

    class Base @Inject constructor(): SearchQueryCommunication, Communication.UiUpdate<String>("")
}