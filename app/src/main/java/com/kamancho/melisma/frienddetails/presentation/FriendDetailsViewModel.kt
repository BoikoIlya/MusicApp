package com.kamancho.melisma.frienddetails.presentation

import androidx.lifecycle.ViewModel
import javax.inject.Inject

/**
 * Created by HP on 18.08.2023.
 **/
class FriendDetailsViewModel @Inject constructor(
    private val queryCommunication: SearchQueryFriendCommunication
): ViewModel() {


    fun saveQueryToCommunication(query: String) = queryCommunication.map(query)
}