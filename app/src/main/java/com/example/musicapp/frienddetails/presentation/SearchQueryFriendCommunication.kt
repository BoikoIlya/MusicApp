package com.example.musicapp.frienddetails.presentation

import com.example.musicapp.app.core.Communication
import javax.inject.Inject

/**
 * Created by HP on 13.08.2023.
 **/
interface SearchQueryFriendCommunication: Communication.Mutable<String> {

    class Base @Inject constructor(): SearchQueryFriendCommunication, Communication.UiUpdate<String>("")
}